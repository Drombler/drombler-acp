package org.drombler.acp.core.status.spi.impl;

import java.util.ArrayList;
import java.util.List;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.acp.core.status.jaxb.StatusBarElementType;
import org.drombler.acp.core.status.jaxb.StatusBarElementsType;
import org.drombler.acp.core.status.spi.StatusBarElementContainer;
import org.drombler.acp.core.status.spi.StatusBarElementContainerProvider;
import org.drombler.acp.core.status.spi.StatusBarElementDescriptor;
import org.drombler.commons.client.geometry.HorizontalAlignment;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.Contexts;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
@Component
public class StatusBarElementHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(StatusBarElementHandler.class);

    private final List<StatusBarElementDescriptor<? extends T>> unresolvedStatusBarElementDescriptors = new ArrayList<>();

    @Reference
    private StatusBarElementContainerProvider<T> statusBarElementContainerProvider;

    @Reference
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    @Reference
    private ContextManagerProvider contextManagerProvider;

    private ContextInjector contextInjector;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindStatusBarElementsType(ServiceReference<StatusBarElementsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        StatusBarElementsType statusBarElementsType = context.getService(serviceReference);
        registerStatusBarElements(statusBarElementsType, context);
    }

    public void unbindStatusBarElementsType(StatusBarElementsType statusBarElementsType) {
        // TODO
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindStatusBarElementDescriptor(StatusBarElementDescriptor<? extends T> statusBarElementDescriptor) {
        registerStatusBarElement(statusBarElementDescriptor);
    }

    public void unbindStatusBarElementDescriptor(StatusBarElementDescriptor<? extends T> statusBarElementDescriptor) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        contextInjector = new ContextInjector(contextManagerProvider.getContextManager());
        resolveUnresolvedStatusBarElementDescriptors();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private boolean isInitialized() {
        return statusBarElementContainerProvider != null && applicationThreadExecutorProvider != null && contextManagerProvider != null && contextInjector != null;
    }

    private void registerStatusBarElements(StatusBarElementsType statusBarElementsType, BundleContext context) {
        statusBarElementsType.getStatusBarElement().forEach(statusBarElementType
                -> registerStatusBarElement(statusBarElementType, context));
    }

    private void registerStatusBarElement(StatusBarElementType statusBarElementType, BundleContext context) {
        try {
            StatusBarElementDescriptor<?> statusBarElementDescriptor = StatusBarElementDescriptor.createStatusBarElementDescriptor(statusBarElementType, context.getBundle());
            context.registerService(StatusBarElementDescriptor.class, statusBarElementDescriptor, null);
        } catch (ClassNotFoundException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private void registerStatusBarElement(StatusBarElementDescriptor<? extends T> statusBarElementDescriptor) {
        if (isInitialized()) {
            registerStatusBarElementInitialized(statusBarElementDescriptor);
        } else {
            unresolvedStatusBarElementDescriptors.add(statusBarElementDescriptor);
        }
    }

    private void registerStatusBarElementInitialized(StatusBarElementDescriptor<? extends T> statusBarElementDescriptor) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> {
            try {
                T statusBarElement = createStatusBarElement(statusBarElementDescriptor);
                PositionableAdapter<? extends T> positionableStatusBarElement = new PositionableAdapter<>(statusBarElement, statusBarElementDescriptor.getPosition());
                StatusBarElementContainer statusBarElementContainer = statusBarElementContainerProvider.getStatusBarElementContainer();
                HorizontalAlignment horizontalAlignment = statusBarElementDescriptor.getHorizontalAlignment().orient(statusBarElementContainer.isLeftToRight(),
                        statusBarElementContainer.isMirroringEnabled());
                addStatusBarElement(statusBarElementContainer, positionableStatusBarElement, horizontalAlignment);
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private T createStatusBarElement(StatusBarElementDescriptor<? extends T> statusBarElementDescriptor) throws InstantiationException, IllegalAccessException {
        T statusBarElement = statusBarElementDescriptor.getStatusBarElementClass().newInstance();
        Contexts.configureObject(statusBarElement, contextManagerProvider.getContextManager(), contextInjector);
        return statusBarElement;
    }

    private void addStatusBarElement(StatusBarElementContainer statusBarElementContainer, PositionableAdapter<? extends T> positionableStatusBarElement, HorizontalAlignment horizontalAlignment) {
        switch (horizontalAlignment) {
            case LEFT:
                statusBarElementContainer.addLeftStatusBarElement(positionableStatusBarElement);
                break;
            case CENTER:
                statusBarElementContainer.addCenterStatusBarElement(positionableStatusBarElement);
                break;
            case RIGHT:
                statusBarElementContainer.addRightStatusBarElement(positionableStatusBarElement);
                break;
            default:
                LOG.error("Unknown horizontalAlignment: " + horizontalAlignment);
                break;
        }
    }

    private void resolveUnresolvedStatusBarElementDescriptors() {
        List<StatusBarElementDescriptor<? extends T>> unresolvedStatusBarElementDescriptorsCopy = new ArrayList<>(unresolvedStatusBarElementDescriptors);
        unresolvedStatusBarElementDescriptors.clear();
        unresolvedStatusBarElementDescriptorsCopy.forEach(this::registerStatusBarElement);
    }
}

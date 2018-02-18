package org.drombler.acp.core.settings.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.settings.jaxb.SettingsType;
import org.drombler.acp.core.settings.spi.AbstractSettingsCategoryDescriptor;
import org.drombler.acp.core.settings.spi.SettingsCategoryContainer;
import org.drombler.acp.core.settings.spi.SettingsCategoryContainerProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
public abstract class AbstractSettingsCategoryHandler<D extends AbstractSettingsCategoryDescriptor> {

    private final List<D> unresolvedSettingsCategoryDescriptors = new ArrayList<>();

    @Reference
    private SettingsCategoryContainerProvider settingsCategoryContainerProvider;

    @Reference
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindSettingsType(ServiceReference<SettingsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        SettingsType settingsType = context.getService(serviceReference);
        registerSettingsType(settingsType, context);
    }

    protected void unbindSettingsType(SettingsType settingsType) {
        // TODO
    }

    protected void activate(ComponentContext context) {
        resolveSettingsCategoryDescriptors();
    }

    protected void deactivate(ComponentContext context) {
    }

    protected abstract void registerSettingsType(SettingsType settingsType, BundleContext context);

    public SettingsCategoryContainer getSettingsCategoryContainer() {
        return settingsCategoryContainerProvider.getSettingsCategoryContainer();
    }

    private void resolveSettingsCategoryDescriptors() {
        unresolvedSettingsCategoryDescriptors.forEach(this::resolveSettingsCategoryDescriptor);
    }

    public Executor getApplicationThreadExecutor() {
        return applicationThreadExecutorProvider.getApplicationThreadExecutor();
    }

    protected void resolveSettingsCategoryDescriptor(D settingsCategoryDescriptor) {
        if (isInitialized()) {
            resolveSettingsCategoryDescriptorInitialized(settingsCategoryDescriptor);
        } else {
            unresolvedSettingsCategoryDescriptors.add(settingsCategoryDescriptor);
        }
    }

    protected boolean isInitialized() {
        return settingsCategoryContainerProvider != null;
    }

    protected abstract void resolveSettingsCategoryDescriptorInitialized(D settingsCategoryDescriptor);

}

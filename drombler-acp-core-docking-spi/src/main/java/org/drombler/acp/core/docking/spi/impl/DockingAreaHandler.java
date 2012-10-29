/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;
import org.drombler.acp.core.docking.jaxb.DockingAreasType;
import org.drombler.acp.core.docking.spi.DockingAreaDescriptor;
import org.drombler.acp.core.docking.spi.DockingAreaFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "dockingAreasType", referenceInterface = DockingAreasType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "dockingAreaDescriptor", referenceInterface = DockingAreaDescriptor.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public class DockingAreaHandler<A, D> extends AbstractDockingHandler<A, D> {

    @Reference
    private DockingAreaFactory<A> dockingAreaFactory;
    private Executor applicationExecutor;
    private final List<DockingAreaDescriptor> unresolvedDockingAreaDescriptors = new ArrayList<>();

    protected void bindDockingAreaFactory(DockingAreaFactory<A> dockingAreaFactory) {
        this.dockingAreaFactory = dockingAreaFactory;
    }

    protected void unbindDockingAreaFactory(DockingAreaFactory<A> dockingAreaFactory) {
        this.dockingAreaFactory = null;
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected void bindDockingAreasType(DockingAreasType dockingAreasType) {
        for (DockingAreaType dockingArea : dockingAreasType.getDockingArea()) {
            DockingAreaDescriptor dockingAreaDescriptor = DockingAreaDescriptor.createDockingAreaDescriptor(dockingArea);
            resolveDockingArea(dockingAreaDescriptor);
        }
    }

    protected void unbindDockingAreasType(DockingAreasType dockingAreasType) {
        // TODO
    }

    protected void bindDockingAreaDescriptor(DockingAreaDescriptor dockingAreaDescriptor) {
        resolveDockingArea(dockingAreaDescriptor);
    }

    protected void unbindDockingAreaDescriptor(DockingAreaDescriptor dockingAreaDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedDockingAreas();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && dockingAreaFactory != null && applicationExecutor != null;
    }

    private void resolveDockingArea(final DockingAreaDescriptor dockingAreaDescriptor) {
        if (isInitialized()) {
            System.out.println(
                    DockingAreaHandler.class.getName() + ": added docking area: " + dockingAreaDescriptor.getId());
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    A dockingArea = dockingAreaFactory.createDockingArea(dockingAreaDescriptor);
                    getDockingAreaContainerProvider().getDockingAreaContainer().addDockingArea(
                            dockingAreaDescriptor.getPath(), dockingArea);
                }
            };
            applicationExecutor.execute(runnable);
        } else {
            unresolvedDockingAreaDescriptors.add(dockingAreaDescriptor);
        }
    }

    private void resolveUnresolvedDockingAreas() {
        for (DockingAreaDescriptor unresolvedDockingAreaDescriptor : unresolvedDockingAreaDescriptors) {
            resolveDockingArea(unresolvedDockingAreaDescriptor);
        }
    }
}

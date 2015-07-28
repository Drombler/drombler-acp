/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.cdi.extension.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.inject.Inject;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.ViewDocking;
import org.ops4j.pax.cdi.api.OsgiService;
import org.softsmithy.lib.util.ServiceProvider;

/**
 * https://docs.jboss.org/weld/reference/latest/en-US/html/extend.html
 *
 * @author puce
 */
@ServiceProvider(serviceClass = Extension.class)
@ApplicationScoped
public class DockingExtension implements Extension {

    @Inject
    @OsgiService
    private ApplicationExecutorProvider applicationExecutorProvider;

    private final List<CreationalInjectionTarget<?>> injectionTargets = new ArrayList<>();
    private final List<NonContextualManagedBean<?>> dockables = new ArrayList<>();

    public DockingExtension() {
        System.out.println("DockingExtension");
    }

    public void readAllConfigurations(final @Observes AfterBeanDiscovery abd, final BeanManager bm) {
        // read application.xml
    }

    public <T> void processAnnotatedType(@Observes @WithAnnotations({ViewDocking.class}) ProcessAnnotatedType<T> event,
            BeanManager beanManager) {
        final AnnotatedType<T> annotatedType = event.getAnnotatedType();
        //The extension uses an InjectionTarget to delegate instantiation, dependency injection
//and lifecycle callbacks to the CDI container
        InjectionTarget<T> it = beanManager.createInjectionTarget(annotatedType);

//each instance needs its own CDI CreationalContext
        CreationalContext<T> ctx = beanManager.createCreationalContext(null);
        injectionTargets.add(new CreationalInjectionTarget<>(it, ctx));

        if (annotatedType.isAnnotationPresent(ViewDocking.class)) {
            System.out.println(annotatedType);
            //get the BeanManager

            //            BeanManager beanManager = CDI.current().getBeanManager(); // TODO: as parameter?
//CDI uses an AnnotatedType object to read the annotations of a class
//        AnnotatedType<S> type = beanManager.createAnnotatedType(annotatedType.getJavaClass() );
        }
    }
//    public <T> void initializePropertyLoading(final @Observes ProcessInjectionTarget<T> pit) {
//        AnnotatedType<T> annotatedType = pit.getAnnotatedType();
//        if (annotatedType.isAnnotationPresent(ViewDocking.class)) {
//
//        }
//    }

    /**
     * AfterDeploymentValidation: All beans are available for injection.
     */
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        injectionTargets.forEach(injectionTarget -> createDockable(injectionTarget, beanManager));
    }

    private <T> void createDockable(CreationalInjectionTarget<T> injectionTarget, BeanManager beanManager) {

        Set<Bean<?>> beans = beanManager.getBeans(ApplicationExecutorProvider.class, new OsgiServiceLiteral());

//            applicationExecutorProvider.getApplicationExecutor().execute(() -> {
        NonContextualManagedBean<T> managedBean = new NonContextualManagedBean<>(injectionTarget);
        managedBean.initialize();

        dockables.add(managedBean);

//            });
    }

    //destroy the framework component instance and clean up dependent objects
    public void beforeShutdown(@Observes BeforeShutdown event) {
        dockables.forEach(dockable -> dockable.destroy());
    }
}

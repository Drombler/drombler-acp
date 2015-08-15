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
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.Unmanaged;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.inject.Inject;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.ViewDocking;
import org.ops4j.pax.cdi.api.OsgiService;

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

    private final List< Unmanaged.UnmanagedInstance<?>> instances = new ArrayList<>();

    public DockingExtension() {
        System.out.println("DockingExtension");
    }

    public void readAllConfigurations(final @Observes AfterBeanDiscovery abd, final BeanManager bm) {
        // read application.xml
    }

    public <T> void processAnnotatedType(@Observes @WithAnnotations({ViewDocking.class}) ProcessAnnotatedType<T> event,
            BeanManager beanManager) {
        final AnnotatedType<T> annotatedType = event.getAnnotatedType();

        Unmanaged<T> unmanaged = new Unmanaged<>(beanManager, annotatedType.getJavaClass());
        Unmanaged.UnmanagedInstance<T> instance = unmanaged.newInstance();
        instances.add(instance);


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
        instances.forEach(instance -> createDockable(instance, beanManager));
    }

    private <T> void createDockable(Unmanaged.UnmanagedInstance<T> instance, BeanManager beanManager) {

        Set<Bean<?>> beans = beanManager.getBeans(ApplicationExecutorProvider.class, new OsgiServiceLiteral());
        Set<Bean<?>> beans2 = beanManager.getBeans(ViewDockingHandler.class, new OsgiServiceLiteral());

        Unmanaged<ViewDockingHandler> u = new Unmanaged<>(beanManager, ViewDockingHandler.class);
        Unmanaged.UnmanagedInstance<ViewDockingHandler> newInstance = u.newInstance();
        newInstance.produce().inject().postConstruct();
//            applicationExecutorProvider.getApplicationExecutor().execute(() -> {
        instance.produce().inject().postConstruct();

//            });
    }

    //destroy the framework component instance and clean up dependent objects
    public void beforeShutdown(@Observes BeforeShutdown event) {
        instances.forEach(instance -> instance.preDestroy().dispose());
    }

}

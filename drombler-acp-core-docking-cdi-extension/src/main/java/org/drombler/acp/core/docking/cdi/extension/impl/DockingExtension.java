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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.ViewDocking;
import org.softsmithy.lib.util.ServiceProvider;

/**
 * https://docs.jboss.org/weld/reference/latest/en-US/html/extend.html
 *
 * @author puce
 */
@ServiceProvider(serviceClass = Extension.class)
@ApplicationScoped
public class DockingExtension implements Extension {

//    @Inject
//    @OsgiService
    private ApplicationExecutorProvider applicationExecutorProvider;

    public DockingExtension() {
        System.out.println("DockingExtension");
    }

    public <T> void processAnnotatedType(@Observes @WithAnnotations({ViewDocking.class}) ProcessAnnotatedType<T> event) {
        final AnnotatedType<T> annotatedType = event.getAnnotatedType();
        if (annotatedType.isAnnotationPresent(ViewDocking.class)) {
            System.out.println(annotatedType);
            //get the BeanManager

            BeanManager beanManager = CDI.current().getBeanManager(); // TODO: as parameter?

//CDI uses an AnnotatedType object to read the annotations of a class
            AnnotatedType<?> type = beanManager.createAnnotatedType((Class<?>) annotatedType.getBaseType());

//The extension uses an InjectionTarget to delegate instantiation, dependency injection
//and lifecycle callbacks to the CDI container
            InjectionTarget<?> it = beanManager.createInjectionTarget(type);

//each instance needs its own CDI CreationalContext
            CreationalContext ctx = beanManager.createCreationalContext(null);

//            applicationExecutorProvider.getApplicationExecutor().execute(() -> {
////instantiate the framework component and inject its dependencies
//                Object instance = it.produce(ctx);  //call the constructor
//
//                it.inject(instance, ctx);  //call initializer methods and perform field injection
//
//                it.postConstruct(instance);  //call the @PostConstruct method
//            });
        }
    }
//    public <T> void initializePropertyLoading(final @Observes ProcessInjectionTarget<T> pit) {
//        AnnotatedType<T> annotatedType = pit.getAnnotatedType();
//        if (annotatedType.isAnnotationPresent(ViewDocking.class)) {
//
//        }
//    }
}

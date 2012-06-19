/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application;

import org.richclientplatform.core.lib.util.ContextListener;
import org.richclientplatform.core.lib.util.Context;
import org.richclientplatform.core.application.Contexts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author puce
 */
public class Activator implements BundleActivator {

    private DefaultContext defaultContext;

    @Override
    public void start(final BundleContext context) throws Exception {
        defaultContext = new DefaultContext(context);
        Contexts.setDefault(defaultContext);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Contexts.setDefault(null);
        defaultContext.close();
    }

    // TODO: useful?
    // TODO: memory leaks? WeakHashMap? Other issues?
    private static class DefaultContext implements Context {

        private final BundleContext context;

        public DefaultContext(BundleContext context) {
            this.context = context;
        }
        private final Map<Class<?>, ServiceTracker<?, ?>> serviceTrackers = new HashMap<>();

        @Override
        public <T> T find(Class<T> type) {
            ServiceTracker<?, ?> serviceTracker = getServiceTracker(type);
            return type.cast(serviceTracker.getService());
        }

        @Override
        public <T> List<? extends T> findAll(Class<T> type) {
            ServiceTracker<?, ?> serviceTracker = getServiceTracker(type);
            Object[] services = serviceTracker.getServices();
            List<T> serviceCollection = new ArrayList<>(services.length);
            for (Object service : services) {
                serviceCollection.add(type.cast(service));
            }
            return serviceCollection;
        }

//        @Override
//        public <T> void track(Class<T> type, final ContextListener<T> listener) {
//            final ServiceTracker<T, T> serviceTracker = getServiceTracker(type, new ServiceTrackerCustomizer<T, T>() {
//
//                @Override
//                public T addingService(ServiceReference<T> reference) {
//                    T service = serviceTracker.getService(reference);
//                    listener.addingToContext(service);
//                    return service;
//                }
//
//                @Override
//                public void modifiedService(ServiceReference<T> reference, T service) {
//                    // not used
//                }
//
//                @Override
//                public void removedService(ServiceReference<T> reference, T service) {
//                    listener.removedFromContext(service);
//                }
//            });

//        }

        private <T> ServiceTracker<?, ?> getServiceTracker(Class<T> type) {
            if (!serviceTrackers.containsKey(type)) {
                ServiceTracker<T, T> serviceTracker = new ServiceTracker<>(context, type, null);
                serviceTracker.open();
                serviceTrackers.put(type, serviceTracker);
            }
            ServiceTracker<?, ?> serviceTracker = serviceTrackers.get(type);
            return serviceTracker;
        }

        public void close() {
            for (ServiceTracker<?, ?> serviceTracker : serviceTrackers.values()) {
                serviceTracker.close();
            }
        }

        @Override
        public void addContextListener(Class<?> type, ContextListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void removeContextListener(Class<?> type, ContextListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

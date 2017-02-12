package org.drombler.acp.core.commons.util;

import java.util.function.Consumer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class SimpleServiceTrackerCustomizer<T> implements ServiceTrackerCustomizer<T, T> {

    private final BundleContext context;
    private final Consumer<T> addingServiceConsumer;
    private final Consumer<T> removedServiceConsumer;

    public SimpleServiceTrackerCustomizer(BundleContext context, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        this.context = context;
        this.addingServiceConsumer = addingServiceConsumer;
        this.removedServiceConsumer = removedServiceConsumer;
    }

    @Override
    public T addingService(ServiceReference<T> reference) {
        T service = context.getService(reference);
        addingServiceConsumer.accept(service);
        return service;
    }

    @Override
    public void modifiedService(ServiceReference<T> reference, T service) {
        addingService(reference);
        removedService(reference, service);
    }

    @Override
    public void removedService(ServiceReference<T> reference, T service) {
        removedServiceConsumer.accept(addingServiceConsumer != removedServiceConsumer ? service : null);
        context.ungetService(reference);
    }

    public static <T> ServiceTracker<T, T> createServiceTracker(Class<T> serviceType, Consumer<T> consumer) {
        return createServiceTracker(serviceType, consumer, consumer);
    }

    public static <T> ServiceTracker<T, T> createServiceTracker(BundleContext bundleContext, Class<T> serviceType, Consumer<T> consumer) {
        return createServiceTracker(bundleContext, serviceType, consumer, consumer);
    }

    public static <T> ServiceTracker<T, T> createServiceTracker(Class<T> serviceType, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        BundleContext bundleContext = FrameworkUtil.getBundle(serviceType).getBundleContext();
        return createServiceTracker(bundleContext, serviceType, addingServiceConsumer, removedServiceConsumer);

    }

    public static <T> ServiceTracker<T, T> createServiceTracker(BundleContext bundleContext, Class<T> serviceType, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        return new ServiceTracker<>(bundleContext, serviceType, new SimpleServiceTrackerCustomizer<>(bundleContext, addingServiceConsumer, removedServiceConsumer));
    }
}

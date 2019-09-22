package org.drombler.acp.core.commons.util;

import java.util.function.Consumer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * A simple {@link ServiceTrackerCustomizer} implementation which delegates the work to {@link Consumer}s.<br>
 * <br>
 * See the static factory methods to create {@link ServiceTracker}s.
 *
 * @param <T> the type of the tracked object
 * @see #createServiceTracker(java.lang.Class, java.util.function.Consumer)
 * @see #createServiceTracker(org.osgi.framework.BundleContext, java.lang.Class, java.util.function.Consumer)
 * @see #createServiceTracker(java.lang.Class, java.util.function.Consumer, java.util.function.Consumer)
 * @see #createServiceTracker(org.osgi.framework.BundleContext, java.lang.Class, java.util.function.Consumer, java.util.function.Consumer)
 * @author puce
 */
public class SimpleServiceTrackerCustomizer<T> implements ServiceTrackerCustomizer<T, T> {

    private final BundleContext context;
    private final Consumer<T> addingServiceConsumer;
    private final Consumer<T> removedServiceConsumer;

    /**
     * Creates a new instance of this class.
     *
     * @param context the bundle context
     * @param addingServiceConsumer the call back which gets called on {@link #addingService(org.osgi.framework.ServiceReference) } and {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     * }
     * @param removedServiceConsumer the call back which gets called on {@link #removedService(org.osgi.framework.ServiceReference, java.lang.Object) } and {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     * }
     */
    public SimpleServiceTrackerCustomizer(BundleContext context, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        this.context = context;
        this.addingServiceConsumer = addingServiceConsumer;
        this.removedServiceConsumer = removedServiceConsumer;
    }

    /**
     * {@inheritDoc }
     *
     * Calls the registered adding service consumer.
     */
    @Override
    public T addingService(ServiceReference<T> reference) {
        T service = context.getService(reference);
        addingServiceConsumer.accept(service);
        return service;
    }

    /**
     * {@inheritDoc }
     *
     * Calls the registered adding and removed service consumer.
     */
    @Override
    public void modifiedService(ServiceReference<T> reference, T service) {
        addingService(reference);
        removedService(reference, service);
    }

    /**
     * {@inheritDoc }
     *
     * Calls the registered removed service consumer.
     */
    @Override
    public void removedService(ServiceReference<T> reference, T service) {
        removedServiceConsumer.accept(addingServiceConsumer != removedServiceConsumer ? service : null);
    }

    /**
     * Creates a service tracker for the specified service type.<br>
     * <br>
     * The provided consumer will be called on {@link #addingService(org.osgi.framework.ServiceReference) },
     * {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object) } and {@link #removedService(org.osgi.framework.ServiceReference, java.lang.Object)}.
     *
     * @param <T> the service type to track
     * @param serviceType the service type to track
     * @param consumer the consumer
     * @return a service tracker for the speficied service type
     */
    public static <T> ServiceTracker<T, T> createServiceTracker(Class<T> serviceType, Consumer<T> consumer) {
        return createServiceTracker(serviceType, consumer, consumer);
    }

    /**
     * Creates a service tracker for the specified service type.<br>
     * <br>
     * The provided addingServiceConsumer will be called on {@link #addingService(org.osgi.framework.ServiceReference) } and {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     * }.<br>
     * The provided removedServiceConsumer will be called on {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object) } and
     * {@link #removedService(org.osgi.framework.ServiceReference, java.lang.Object)}.
     *
     * @param <T> the service type to track
     * @param serviceType the service type to track
     * @param addingServiceConsumer the adding service consumer
     * @param removedServiceConsumer the removed service consumer
     * @return a service tracker for the speficied service type
     */
    public static <T> ServiceTracker<T, T> createServiceTracker(Class<T> serviceType, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        BundleContext bundleContext = FrameworkUtil.getBundle(serviceType).getBundleContext();
        return createServiceTracker(bundleContext, serviceType, addingServiceConsumer, removedServiceConsumer);

    }

    /**
     * Creates a service tracker for the specified service type.<br>
     * <br>
     * The provided consumer will be called on {@link #addingService(org.osgi.framework.ServiceReference) },
     * {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object) } and {@link #removedService(org.osgi.framework.ServiceReference, java.lang.Object)}.
     *
     * @param <T> the service type to track
     * @param bundleContext the bundle context
     * @param serviceType the service type to track
     * @param consumer the consumer
     * @return a service tracker for the speficied service type
     */
    public static <T> ServiceTracker<T, T> createServiceTracker(BundleContext bundleContext, Class<T> serviceType, Consumer<T> consumer) {
        return createServiceTracker(bundleContext, serviceType, consumer, consumer);
    }

    /**
     * Creates a service tracker for the specified service type.<br>
     * <br>
     * The provided addingServiceConsumer will be called on {@link #addingService(org.osgi.framework.ServiceReference) } and {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     * }.<br>
     * The provided removedServiceConsumer will be called on {@link #modifiedService(org.osgi.framework.ServiceReference, java.lang.Object) } and
     * {@link #removedService(org.osgi.framework.ServiceReference, java.lang.Object)}.
     *
     * @param <T> the service type to track
     * @param bundleContext the bundle context
     * @param serviceType the service type to track
     * @param addingServiceConsumer the adding service consumer
     * @param removedServiceConsumer the removed service consumer
     * @return a service tracker for the speficied service type
     */
    public static <T> ServiceTracker<T, T> createServiceTracker(BundleContext bundleContext, Class<T> serviceType, Consumer<T> addingServiceConsumer, Consumer<T> removedServiceConsumer) {
        return new ServiceTracker<>(bundleContext, serviceType, new SimpleServiceTrackerCustomizer<>(bundleContext, addingServiceConsumer, removedServiceConsumer));
    }
}

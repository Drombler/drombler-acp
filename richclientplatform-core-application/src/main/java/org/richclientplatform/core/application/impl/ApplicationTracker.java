/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.richclientplatform.core.application.ExtensionPoint;
import org.richclientplatform.core.application.jaxb.ApplicationType;
import org.richclientplatform.core.application.jaxb.ExtensionsType;


/**
 *
 * @author puce
 */
@Component
@Reference(name = "extensionPoint", referenceInterface = ExtensionPoint.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ApplicationTracker {

    private BundleTracker<ApplicationType> bundleTracker;
    private Class[] jaxbRootClasses = new Class[]{ApplicationType.class};
    private final Map<Long, List<ServiceRegistration<?>>> serviceRegistrations = new HashMap<>();
//    private final ServiceTracker<ExtensionPoint, ExtensionPoint> serviceTracker;
//    private final Map<Class<?>, ExtensionPoint<?>> applicationExtensionHandlers = new HashMap<>();

//    public ApplicationTracker() {
//        bundleTracker = null;
//
//    }
//    @Reference
//    private List<ExtensionPoint<?>> extensionPoints;
    @Activate
    public void activate(BundleContext context) {
        bundleTracker = new BundleTracker<>(context, Bundle.ACTIVE,
                new BundleTrackerCustomizer<ApplicationType>() {

                    @Override
                    public ApplicationType addingBundle(Bundle bundle, BundleEvent event) {
                        URL actionsURL = bundle.getEntry("META-INF/platform/application.xml");
                        if (actionsURL != null) {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(jaxbRootClasses);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                ApplicationType application = (ApplicationType) unmarshaller.unmarshal(actionsURL);
                                registerExtensions(bundle, application.getExtensions());
//                                List<ActionDescriptor> actionDescriptors = new ArrayList<>(actions.getAction().size());
//                                for (ActionType actionType : actions.getAction()) {
//                                    ActionDescriptor actionDescriptor = ActionDescriptor.createActionDescriptor(
//                                            actionType, bundle);
//                                    actionDescriptors.add(actionDescriptor);
//                                }
//                                actionTrackerListener.addingExtension(bundle, event, actionDescriptors);
                                return application;
                            } catch (JAXBException ex) {
                                // TODO: ???
                                Logger.getLogger(ApplicationTracker.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        return null;
                    }

                    @Override
                    public void modifiedBundle(Bundle bundle, BundleEvent event, ApplicationType application) {
                        // TODO: ???
                    }

                    @Override
                    public void removedBundle(Bundle bundle, BundleEvent event, ApplicationType application) {
                        unregisterExtensions(bundle);
                    }
                });
        bundleTracker.open();
    }

    @Deactivate
    public void deactivate() {
        bundleTracker.close();
    }

    private void registerExtensions(Bundle bundle, ExtensionsType extensions) {
        List<ServiceRegistration<?>> registrations = new ArrayList<>(extensions.getAny().size());
        for (Object extension : extensions.getAny()) {
            ServiceRegistration<?> serviceRegistration = bundle.getBundleContext().registerService(
                    extension.getClass().getName(), extension, null);
            registrations.add(serviceRegistration);
        }
        serviceRegistrations.put(bundle.getBundleId(), registrations);
    }

    private void unregisterExtensions(Bundle bundle) {
        if (serviceRegistrations.containsKey(bundle.getBundleId())) {
            for (ServiceRegistration<?> serviceRegistration : serviceRegistrations.remove(bundle.getBundleId())) {
                serviceRegistration.unregister();
            }
        }
    }

//    public ApplicationTracker(BundleContext context, final ExtensionTrackerListener<List<ActionDescriptor>> actionTrackerListener) {
//    public ApplicationTracker(final BundleContext context) {
//        serviceTracker = new ServiceTracker<ExtensionPoint, ExtensionPoint>(context,
//                ExtensionPoint.class,
//                new ServiceTrackerCustomizer<ExtensionPoint, ExtensionPoint>() {
//
//                    @Override
//                    public ExtensionPoint addingService(ServiceReference<ExtensionPoint> reference) {
//                        ExtensionPoint applicationExtensionHandler = context.getService(reference);
//                        applicationExtensionHandlers.put(applicationExtensionHandler.getJAXBRootClass(),
//                                applicationExtensionHandler);
//                        return applicationExtensionHandler;
//                    }
//
//                    @Override
//                    public void modifiedService(ServiceReference<ExtensionPoint> reference, ExtensionPoint service) {
//                        // TODO: ???
//                    }
//
//                    @Override
//                    public void removedService(ServiceReference<ExtensionPoint> reference, ExtensionPoint service) {
//                        applicationExtensionHandlers.remove(service.getJAXBRootClass());
//                    }
//                });
//
//    }
//    public void open() {
////        serviceTracker.open();
//        bundleTracker.open();
//    }
//
//    public void close() {
////        bundleTracker.close();
////        serviceTracker.close();
//    }
    public void bindExtensionPoint(ExtensionPoint<?> extensionPoint) {
        List<Class> classes = new ArrayList<>(Arrays.asList(jaxbRootClasses));
        classes.add(extensionPoint.getJAXBRootClass());
        jaxbRootClasses = classes.toArray(new Class[classes.size()]);
    }

    public void unbindExtensionPoint(ExtensionPoint<?> extensionPoint) {
        List<Class> classes = new ArrayList<>(Arrays.asList(jaxbRootClasses));
        classes.remove(extensionPoint.getJAXBRootClass());
        jaxbRootClasses = classes.toArray(new Class[classes.size()]);
//        unregisterExtensions
    }
//    private void hanldeAddingExtensions(Bundle bundle, BundleEvent event, ExtensionsType extensions) {
//        for (Object extension : extensions.getAny()) {
//            if (applicationExtensionHandlers.containsKey(extension.getClass())) {
//                ExtensionPoint<?> applicationExtensionHandler = applicationExtensionHandlers.get(
//                        extension.getClass());
////                handleAddingExtension(bundle, event, applicationExtensionHandler, extension);
//            }
//        }
//    }
//    private <T> void handleAddingExtension(Bundle bundle, BundleEvent event, ApplicationExtensionHandler<T> applicationExtensionHandler, Object extension) {
//        applicationExtensionHandler.addingExtension(bundle, event, applicationExtensionHandler.getJAXBRootClass().cast(
//                extension));
//    }
//    @Override
//    public ExtensionPoint getService(Bundle bundle, ServiceRegistration<ExtensionPoint> registration) {
//
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void ungetService(Bundle bundle, ServiceRegistration<ExtensionPoint> registration, ExtensionPoint service) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}

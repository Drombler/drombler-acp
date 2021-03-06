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
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.application.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.acp.core.application.jaxb.ApplicationType;
import org.drombler.acp.core.application.jaxb.ExtensionsType;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component
public class ApplicationTracker {

    public static final String APPLICATION_XML_RELATIVE_NAME = "META-INF/drombler/application.xml";

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationTracker.class);

    private BundleTracker<ApplicationType> bundleTracker;
    private final Set<Class<?>> jaxbRootClassesSet = new HashSet<>(Arrays.asList(ApplicationType.class));
    private Class[] jaxbRootClasses = new Class[]{ApplicationType.class};
    private final Map<Long, List<ServiceRegistration<?>>> serviceRegistrations = new HashMap<>();
    // TODO: thread-safe? memory leak?
    private final Set<Bundle> unresolvedExtensions = new LinkedHashSet<>();
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
                        return registerExtensions(bundle);
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

    private ApplicationType registerExtensions(Bundle bundle) {
        URL actionsURL = bundle.getEntry(APPLICATION_XML_RELATIVE_NAME);
        if (actionsURL != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(jaxbRootClasses);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ApplicationType application = (ApplicationType) unmarshaller.unmarshal(actionsURL);
                if (loadedExtensionsSuccessfully(application.getExtensions())) {
                    if (unresolvedExtensions.contains(bundle)) {
                        unresolvedExtensions.remove(bundle);
                    }
                    registerExtensions(bundle, application.getExtensions());
                    return application;
                } else {
                    if (!unresolvedExtensions.contains(bundle)) {
                        unresolvedExtensions.add(bundle);
                    }
                }
//                List<ActionDescriptor> actionDescriptors = new ArrayList<>(actions.getAction().size());
//                for (ActionType actionType : actions.getAction()) {
//                    ActionDescriptor actionDescriptor = ActionDescriptor.createActionDescriptor(
//                            actionType, bundle);
//                    actionDescriptors.add(actionDescriptor);
//                }
//                actionTrackerListener.addingExtension(bundle, event, actionDescriptors);
            } catch (JAXBException ex) {
                // TODO: ???
                LOG.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    private boolean loadedExtensionsSuccessfully(ExtensionsType extensions) {
        boolean successfullyLoaded = true;
        for (Object extension : extensions.getAny()) {
            if (!jaxbRootClassesSet.contains(extension.getClass())) {
                successfullyLoaded = false;
                break;
            }
        }
        return successfullyLoaded;
    }

    private void registerExtensions(Bundle bundle, ExtensionsType extensions) {
        final List<ServiceRegistration<?>> registrations = extensions.getAny().stream().
                map(extension
                        -> bundle.getBundleContext().registerService(extension.getClass().getName(), extension, null)).
                collect(Collectors.toList());
        serviceRegistrations.put(bundle.getBundleId(), registrations);
    }

    private void unregisterExtensions(Bundle bundle) {
        if (serviceRegistrations.containsKey(bundle.getBundleId())) {
            serviceRegistrations.remove(bundle.getBundleId()).forEach(serviceRegistration
                    -> serviceRegistration.unregister());
        }
        if (unresolvedExtensions.contains(bundle)) {
            unresolvedExtensions.remove(bundle);
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
        jaxbRootClassesSet.add(extensionPoint.getJAXBRootClass());
        jaxbRootClasses = new ArrayList<>(jaxbRootClassesSet).toArray(new Class[jaxbRootClassesSet.size()]);
        if (!unresolvedExtensions.isEmpty()) {
            // avoid concurrent modification // TODO: needed here?
            List<Bundle> extensionBundles = new ArrayList<>(unresolvedExtensions);
            extensionBundles.forEach(extensionBundle -> registerExtensions(extensionBundle));
        }
    }

    public void unbindExtensionPoint(ExtensionPoint<?> extensionPoint) {
        jaxbRootClassesSet.remove(extensionPoint.getJAXBRootClass());
        jaxbRootClasses = new ArrayList<>(jaxbRootClassesSet).toArray(new Class[jaxbRootClassesSet.size()]);
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

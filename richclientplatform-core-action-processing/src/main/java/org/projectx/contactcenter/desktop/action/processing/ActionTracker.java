/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.projectx.contactcenter.desktop.action.processing;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.Actions;

/**
 *
 * @author puce
 */
public class ActionTracker {

    private final BundleTracker<List<ActionDescriptor>> bundleTracker;

    public ActionTracker(BundleContext context, final ExtensionTrackerListener<List<ActionDescriptor>> actionTrackerListener) {
        bundleTracker = new BundleTracker<>(context, Bundle.ACTIVE, new BundleTrackerCustomizer<List<ActionDescriptor>>() {

            @Override
            public List<ActionDescriptor> addingBundle(Bundle bundle, BundleEvent event) {
                URL actionsURL = bundle.getEntry("META-INF/platform/actions.xml");
                if (actionsURL != null) {
                    try {
                        JAXBContext jaxbContext = JAXBContext.newInstance(Actions.class);
                        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                        Actions actions = (Actions) unmarshaller.unmarshal(actionsURL);
                        List<ActionDescriptor> actionDescriptors = new ArrayList<>(actions.getAction().size());
                        for (ActionType actionType : actions.getAction()) {
                            ActionDescriptor actionDescriptor = ActionDescriptor.createActionDescriptor(actionType, bundle);
                            actionDescriptors.add(actionDescriptor);
                        }
                        actionTrackerListener.addingExtension(bundle, event, actionDescriptors);
                        return actionDescriptors;
                    } catch (JAXBException | ClassNotFoundException ex) {
                        // TODO: ???
                        Logger.getLogger(ActionTracker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return null;
            }

            @Override
            public void modifiedBundle(Bundle bundle, BundleEvent event, List<ActionDescriptor> actionDescriptors) {
                // TODO: ???
            }

            @Override
            public void removedBundle(Bundle bundle, BundleEvent event, List<ActionDescriptor> actionDescriptors) {
                actionTrackerListener.removedExtension(bundle, event, actionDescriptors);
            }
        });
    }
    
    public void open(){
        bundleTracker.open();
    }
    
    public void close(){
        bundleTracker.close();
    }
}

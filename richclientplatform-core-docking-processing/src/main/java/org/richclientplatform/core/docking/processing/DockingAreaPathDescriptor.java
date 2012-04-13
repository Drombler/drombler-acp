/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import java.util.EnumMap;
import java.util.Map;
import org.richclientplatform.core.docking.jaxb.DockingAreaPathType;
import org.richclientplatform.core.docking.jaxb.OrientationType;
import org.richclientplatform.core.lib.geometry.Orientation;

/**
 *
 * @author puce
 */
public class DockingAreaPathDescriptor {
    //TODO: not safe as changes to OrientationType are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from Orientation and omit OrientationType
    // - Add a method toJaxb() to Orientation -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when writing the XML (changes to Orientation are missed at compile time)

    private static final Map<OrientationType, Orientation> ORIENTATIONS = new EnumMap<>(OrientationType.class);

    static {

        ORIENTATIONS.put(OrientationType.HORIZONTAL, Orientation.HORIZONTAL);
        ORIENTATIONS.put(OrientationType.VERTICAL, Orientation.VERTICAL);
        for (OrientationType orientation : OrientationType.values()) {
            if (!ORIENTATIONS.containsKey(orientation)) {
                throw new IllegalStateException("No mapping for: " + orientation);
            }
        }
    }
    private Orientation orientation;
    private int position;

    /**
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public static DockingAreaPathDescriptor createDockingAreaPathDescriptor(DockingAreaPathType path) {
        DockingAreaPathDescriptor pathDescriptor = new DockingAreaPathDescriptor();
        pathDescriptor.setOrientation(ORIENTATIONS.get(path.getOrientation()));
        pathDescriptor.setPosition(path.getPosition());
        return pathDescriptor;
    }
}

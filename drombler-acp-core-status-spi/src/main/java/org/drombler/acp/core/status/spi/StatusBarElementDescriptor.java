package org.drombler.acp.core.status.spi;

import java.util.EnumMap;
import java.util.Map;
import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;
import org.drombler.acp.core.status.jaxb.HorizontalAlignmentType;
import org.drombler.acp.core.status.jaxb.StatusBarElementType;
import org.drombler.commons.client.geometry.HorizontalAlignment;
import org.osgi.framework.Bundle;

/**
 * A status bar element descriptor.
 *
 * @author puce
 * @param <T> the type of the status bar element
 */
public class StatusBarElementDescriptor<T> {
    //TODO: not safe as changes to DockingAreaKind are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from DockingAreaKind and omit DockingAreaKindType
    // - Add a method toJaxb() to DockingAreaKind -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when writing the XML in the annotation processor (changes to DockingAreaKind are missed at compile time)
    private static final Map<HorizontalAlignmentType, HorizontalAlignment> HORIZONTAL_ALIGNMENT_MAP = new EnumMap<>(HorizontalAlignmentType.class);

    static {
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignmentType.LEFT, HorizontalAlignment.LEFT);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignmentType.CENTER, HorizontalAlignment.CENTER);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignmentType.RIGHT, HorizontalAlignment.RIGHT);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignmentType.LEADING, HorizontalAlignment.LEADING);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignmentType.TRAILING, HorizontalAlignment.TRAILING);
        for (HorizontalAlignmentType dockingAreaKind : HorizontalAlignmentType.values()) {
            if (!HORIZONTAL_ALIGNMENT_MAP.containsKey(dockingAreaKind)) {
                throw new IllegalStateException("No mapping for: " + dockingAreaKind);
            }
        }
    }

    private final Class<T> statusBarElementClass;
    private final HorizontalAlignment horizontalAlignment;
    private final int position;

    /**
     * Creates a new instance of this class.
     *
     * @param statusBarElementClass the status bar element class
     * @param horizontalAlignment the horizontal alignment
     * @param position the preferred position
     */
    public StatusBarElementDescriptor(Class<T> statusBarElementClass, HorizontalAlignment horizontalAlignment, int position) {
        this.statusBarElementClass = statusBarElementClass;
        this.horizontalAlignment = horizontalAlignment;
        this.position = position;
    }

    /**
     * Gets the status bar element class.
     *
     * @return the status bar element class
     */
    public Class<T> getStatusBarElementClass() {
        return statusBarElementClass;
    }

    /**
     * Gets the horizontal alignment.
     *
     * @return the horizontal alignment
     */
    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Gets the preferred position.
     *
     * @return the preferred position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Creates an instance of a {@link StatusBarElementDescriptor} from a {@link StatusBarElementType} unmarshalled from the application.xml.
     *
     * @param statusBarElement the unmarshalled status bar element
     * @param bundle the bundle of the application.xml
     * @return a ToggleMenuEntryDescriptor
     * @throws java.lang.ClassNotFoundException
     */
    public static StatusBarElementDescriptor<?> createStatusBarElementDescriptor(StatusBarElementType statusBarElement, Bundle bundle) throws ClassNotFoundException {
        Class<?> statusBarElementClass = loadClass(bundle, statusBarElement.getStatusBarElementClass());
        return createStatusBarElementDescriptor(statusBarElement, statusBarElementClass);
    }

    private static <T> StatusBarElementDescriptor<T> createStatusBarElementDescriptor(StatusBarElementType statusBarElement, Class<T> statusBarElementClass) {
        return new StatusBarElementDescriptor<>(statusBarElementClass, HORIZONTAL_ALIGNMENT_MAP.get(statusBarElement.getHorizontalAlignment()),
                 statusBarElement.getPosition());
    }

}

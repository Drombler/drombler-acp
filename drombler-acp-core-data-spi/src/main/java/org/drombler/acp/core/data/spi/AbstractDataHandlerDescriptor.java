package org.drombler.acp.core.data.spi;

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.jaxb.DataHandlerType;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.ResourceLoader;

public abstract class AbstractDataHandlerDescriptor<D> {

    private String icon;
    private Class<D> dataHandlerClass;
    private ResourceLoader resourceLoader;

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Class<D> getDataHandlerClass() {
        return dataHandlerClass;
    }

    /**
     * @param dataHandlerClass the dataHandlerClass to set
     */
    public void setDataHandlerClass(Class<D> dataHandlerClass) {
        this.dataHandlerClass = dataHandlerClass;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected static Class<?> loadHandlerClass(DataHandlerType dataHandlerType, Bundle bundle) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(dataHandlerType.getHandlerClass()));
    }

    protected static <D> void configureDataHandlerDescriptor(AbstractDataHandlerDescriptor<D> descriptor, DataHandlerType dataHandlerType, Class<D> handlerClass) {
        descriptor.setIcon(StringUtils.stripToNull(dataHandlerType.getIcon()));
        descriptor.setResourceLoader(new ResourceLoader(handlerClass));
        descriptor.setDataHandlerClass(handlerClass);
    }
}

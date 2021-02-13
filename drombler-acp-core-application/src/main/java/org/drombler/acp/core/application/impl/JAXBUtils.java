package org.drombler.acp.core.application.impl;

import org.drombler.acp.core.application.jaxb.ObjectFactory;
import org.osgi.framework.Bundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Set;

public final class JAXBUtils {

    private JAXBUtils(){}

    /**
     * Creates a JAXB context.
     *
     * @return a JAXB context
     * @throws JAXBException
     */
    public static JAXBContext createJAXBContext(Set<String> jaxbPackages, Bundle bundle) throws JAXBException {
        return JAXBContext.newInstance(calculateContextPath(jaxbPackages), ObjectFactory.class.getClassLoader());// BundleUtils.getClassLoader(bundle));
    }

    private static String calculateContextPath(Set<String> jaxbPackages) {
        return String.join(":", jaxbPackages.toArray(new String[jaxbPackages.size()]));
    }
}

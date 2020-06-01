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
package org.drombler.acp.core.application.processing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.acp.core.application.impl.ApplicationTracker;
import org.drombler.acp.core.application.jaxb.ApplicationType;
import org.drombler.acp.core.application.jaxb.ExtensionsType;

/**
 * A base class for annotation processors to register extensions in the application.xml file.<br>
 * <br>
 * Implementations must add:
 * <ul>
 * <li>an instance of the JAXB root element: {@link #addExtensionConfiguration(java.lang.Object)} </li>
 * <li>the model elements with the annotations: {@link #addOriginatingElements(javax.lang.model.element.Element...)} </li>
 * <li>either the JAXB root element type: {@link #addJAXBRootClass(java.lang.Class)} or the package of the JAXB root element type: {@link #addJAXBPackage(java.lang.String) }</li>
 * </ul>
 *
 * @author puce
 */
public abstract class AbstractApplicationAnnotationProcessor extends AbstractProcessor {

    private static final Set<String> JAXB_PACKAGES = new HashSet<>();
    private static final List<Object> EXTENSION_CONFIGURATIONS = new ArrayList<>();
    private static final List<Element> ORIGINATING_ELEMENTS = new ArrayList<>();
    private static FileObject APPLICATION_FILE_OBJECT = null;

    static {
        addJAXBRootClass(ApplicationType.class);
    }

    /**
     * Creates a new instance of this class.
     */
    public AbstractApplicationAnnotationProcessor() {
    }

    /**
     * Calls {@link #handleProcess(java.util.Set, javax.annotation.processing.RoundEnvironment) } and then writes the application file, if the processing is over.
     *
     * @param annotations the annotations
     * @param roundEnv the round environment
     * @return true, if the set of annotations is claimed, else false
     * @see RoundEnvironment#processingOver()
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean claimed = handleProcess(annotations, roundEnv);
        if (roundEnv.processingOver()) {
            writeApplicationFile();
        }
        return claimed;
    }

    /**
     * Handles the specific annotations. <br>
     * <br>
     * Implementations must add:
     * <ul>
     * <li>{@link #addExtensionConfiguration(java.lang.Object)} </li>
     * <li>{@link #addOriginatingElements(javax.lang.model.element.Element...)} </li>
     * <li>either {@link #addJAXBRootClass(java.lang.Class)} or {@link #addJAXBPackage(java.lang.String) }</li>
     * </ul>
     *
     * @param annotations the annotations
     * @param roundEnv the round environment
     * @return true, if the set of annotations is claimed, else false
     */
    protected abstract boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    private void writeApplicationFile() {
        if (APPLICATION_FILE_OBJECT == null) {
            Filer filer = processingEnv.getFiler();
            Messager messager = processingEnv.getMessager();
            try {
                JAXBContext jaxbContext = createJAXBContext();
                ApplicationType application = readManualApplicationFile(filer, jaxbContext);
                if (application == null) {
                    application = new ApplicationType();
                }
                if (application.getExtensions() == null) {
                    ExtensionsType extensions = new ExtensionsType();
                    application.setExtensions(extensions);
                }
                application.getExtensions().getAny().addAll(EXTENSION_CONFIGURATIONS);
                writeApplicationFile(filer, jaxbContext, application);
            } catch (JAXBException | IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            }
        }
    }

    /**
     * Creates a JAXB context.
     *
     * @return a JAXB context
     * @throws JAXBException
     */
    protected JAXBContext createJAXBContext() throws JAXBException {
        return JAXBContext.newInstance(String.join(":", JAXB_PACKAGES.toArray(new String[JAXB_PACKAGES.size()])), ApplicationType.class.getClassLoader());
    }

    private void writeApplicationFile(Filer filer, JAXBContext jaxbContext, ApplicationType application) throws JAXBException, IOException {
        APPLICATION_FILE_OBJECT = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", ApplicationTracker.APPLICATION_XML_RELATIVE_NAME,
                ORIGINATING_ELEMENTS.toArray(new Element[ORIGINATING_ELEMENTS.size()]));
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        try (Writer writer = APPLICATION_FILE_OBJECT.openWriter()) {
            marshaller.marshal(application, writer);
        }
    }

    private ApplicationType readManualApplicationFile(Filer filer, JAXBContext jaxbContext) throws IOException, JAXBException {
        FileObject applicationXmlFileObject = getManualApplicationXml(filer);
        if (applicationXmlFileObject != null) {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            try (InputStream is = applicationXmlFileObject.openInputStream()) {
                return (ApplicationType) unmarshaller.unmarshal(is);
            } catch (FileNotFoundException | NoSuchFileException ex) { // TODO: is FileNotFoundException still possible?
                return null;
            }
        } else {
            return null;
        }
    }

    private FileObject getManualApplicationXml(Filer filer) {
        try {
            FileObject fileObject = filer.getResource(StandardLocation.CLASS_OUTPUT, "", ApplicationTracker.APPLICATION_XML_RELATIVE_NAME);
//            if (fileObject.toUri().equals(ORIGINATING_ELEMENTS.get(0).)){
            return fileObject;
//            } else {
//                return null;
//            }
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Adds the JAXB root class of the extension point.
     *
     * @param jaxbRootClass the JAXB root class of the extension point
     * @see ExtensionPoint#getJAXBRootClass()
     */
    protected static void addJAXBRootClass(Class<?> jaxbRootClass) {
        JAXB_PACKAGES.add(jaxbRootClass.getPackage().getName());
    }

    /**
     * Adds the JAXB package of the extension point.
     *
     * @param jaxbPackageName the JAXB package name of the extension point
     * @see ExtensionPoint#getJAXBRootClass()
     */
    protected static void addJAXBPackage(String jaxbPackageName) {
        JAXB_PACKAGES.add(jaxbPackageName);
    }

    /**
     * Adds an extension configuration (an instance of a JAXB root class of the extension point).
     *
     * @param extensionConfiguration an extension configuration to add
     * @see #addJAXBRootClass(java.lang.Class)
     */
    protected static void addExtensionConfiguration(Object extensionConfiguration) {
        EXTENSION_CONFIGURATIONS.add(extensionConfiguration);
    }

    /**
     * Adds the originating elements.
     *
     * @param originatingElements the originating elements
     */
    protected static void addOriginatingElements(Element... originatingElements) {
        ORIGINATING_ELEMENTS.addAll(Arrays.asList(originatingElements));
    }
}

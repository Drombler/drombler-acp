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
package org.drombler.acp.core.application;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.drombler.acp.core.application.jaxb.ApplicationType;
import org.drombler.acp.core.application.jaxb.ExtensionsType;

/**
 *
 * @author puce
 */
public abstract class AbstractApplicationAnnotationProcessor extends AbstractProcessor {

    private static final String APPLICATION_XML_RELATIVE_NAME = "META-INF/platform/application.xml";
    private static final List<Class<?>> JAXB_ROOT_CLASSES = new ArrayList<Class<?>>(Arrays.asList(ApplicationType.class));
    private static final List<Object> EXTENSION_CONFIGURATIONS = new ArrayList<>();
    private static final List<Object> ORIGINATING_ELEMENTS = new ArrayList<>();
    private static FileObject APPLICATION_FILE_OBJECT = null;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean claimed = handleProcess(annotations, roundEnv);
        if (roundEnv.processingOver()) {
            writeApplicationFile();
        }
        return claimed;
    }

    protected abstract boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    private void writeApplicationFile() {
        Filer filer = processingEnv.getFiler();
        Messager messager = processingEnv.getMessager();


//            try {
//                actionsFileObject = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", APPLICATION_XML_RELATIVE_NAME);
//            } catch (Exception ex) {
//                // TODO: needed?
//                // The Javadoc says an IOException should be thrown, if the file does not exist, but it isn't thrown currently. 
//                //Bug which might be fixed in a future Java version?
//            }
        if (APPLICATION_FILE_OBJECT == null) {
            try {
                APPLICATION_FILE_OBJECT = filer.createResource(
                        StandardLocation.SOURCE_OUTPUT,
                        "", APPLICATION_XML_RELATIVE_NAME,
                        ORIGINATING_ELEMENTS.toArray(new Element[ORIGINATING_ELEMENTS.size()]));
                JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_ROOT_CLASSES.toArray(
                        new Class[JAXB_ROOT_CLASSES.size()]));
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                ApplicationType application = new ApplicationType();
                ExtensionsType extensions = new ExtensionsType();
                extensions.getAny().addAll(EXTENSION_CONFIGURATIONS);
                application.setExtensions(extensions);
                try (Writer writer = APPLICATION_FILE_OBJECT.openWriter()) {
                    marshaller.marshal(application, writer);
                }

//                 try {
//                String packageName = Action.class.getPackage().getName() + ".gen";
//                String className = "Actions";
//                JavaFileObject javaFileObject = filer.createSourceFile(packageName + "." + className,
//                        elements.toArray(new Element[elements.size()]));
//                try (Writer writer = javaFileObject.openWriter()) {
//                    writer.write("package " + packageName + ";\n");
//                    writer.write("import " + Application.class.getName() + ";\n");
//                    writer.write("@" + Application.class.getSimpleName() + "(\""+actionsFileObject.toUri() +"\")\n");
//                    writer.write("public class " + className + "{}\n");
//                }
//        } catch (IOException ex) {
//            Logger.getLogger(ActionAnnotationProcessor.class.getName()).log(Level.SEVERE, null, ex);
//        }
            } catch (JAXBException | IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            }
        }
    }

    protected static void addJAXBRootClasses(Class<?> jaxbRootClasses) {
        JAXB_ROOT_CLASSES.add(jaxbRootClasses);
    }

    protected static void addExtensionConfigurations(Object extensionConfigurations) {
        EXTENSION_CONFIGURATIONS.add(extensionConfigurations);
    }

    protected static void addOriginatingElements(Element... originatingElements) {
        ORIGINATING_ELEMENTS.addAll(Arrays.asList(originatingElements));
    }
}

/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is provided by Drombler GmbH. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2020 Drombler GmbH. All Rights Reserved.
 *
 * Contributor(s): .
 */

import org.drombler.acp.core.data.impl.DataHandlerAnnotationProcessor;
import org.drombler.acp.core.data.impl.FileExtensionAnnotationProcessor;

import javax.annotation.processing.Processor;
module org.drombler.acp.core.data {
    exports org.drombler.acp.core.data;
    exports org.drombler.acp.core.data.jaxb;

    opens org.drombler.acp.core.data.jaxb to java.xml.bind;

    provides Processor with DataHandlerAnnotationProcessor, FileExtensionAnnotationProcessor;

    requires transitive java.desktop;
    requires java.compiler;
    requires org.drombler.acp.core.application;
    requires org.drombler.acp.core.commons;
    requires transitive org.drombler.commons.data.core;
    requires transitive org.softsmithy.lib.core;
    requires osgi.cmpn;
    requires org.apache.commons.lang3;
    requires osgi.core;
}

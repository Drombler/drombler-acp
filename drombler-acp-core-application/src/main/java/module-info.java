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

import org.drombler.acp.core.application.impl.ExtensionAnnotationProcessor;

import javax.annotation.processing.Processor;

/**
 *
 */
module org.drombler.acp.core.application {
    exports org.drombler.acp.core.application;
    exports org.drombler.acp.core.application.jaxb;
    exports org.drombler.acp.core.application.processing;

    opens org.drombler.acp.core.application.jaxb to java.xml.bind;

    provides Processor with ExtensionAnnotationProcessor;

    requires org.drombler.acp.core.commons;
    requires org.softsmithy.lib.compiler;
    requires org.slf4j;
    requires osgi.core;
    requires osgi.cmpn;
    requires transitive java.xml.bind;
    requires transitive java.annotation;
    requires jakarta.activation;
    requires static osgi.annotation;
}

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

import javax.annotation.processing.Processor;
import org.drombler.acp.core.docking.impl.DockingAnnotationProcessor;
import org.drombler.acp.core.docking.impl.DockingAreaAnnotationProcessor;

module org.drombler.acp.core.docking {
    exports org.drombler.acp.core.docking;
    exports org.drombler.acp.core.docking.jaxb;

    provides Processor with DockingAreaAnnotationProcessor, DockingAnnotationProcessor;

    requires java.compiler;
    requires org.drombler.commons.docking.core;
    requires org.drombler.acp.core.application;
    requires org.softsmithy.lib.compiler;
    requires org.apache.commons.lang3;
}

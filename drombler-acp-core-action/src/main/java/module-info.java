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
import org.drombler.acp.core.action.impl.ActionAnnotationProcessor;
import org.drombler.acp.core.action.impl.MenuAnnotationProcessor;
import org.drombler.acp.core.action.impl.ToolBarAnnotationProcessor;
module org.drombler.acp.core.action {
    exports org.drombler.acp.core.action;

    provides Processor with ActionAnnotationProcessor, MenuAnnotationProcessor, ToolBarAnnotationProcessor;

    requires java.compiler;
    requires org.drombler.acp.core.application;
    requires transitive org.softsmithy.lib.core;
    requires org.apache.commons.lang3;
}

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

module org.drombler.acp.core.docking.spi {
    exports org.drombler.acp.core.docking.spi;

    requires org.drombler.acp.core.docking;
    requires org.drombler.acp.core.data.spi;
    requires org.drombler.acp.core.action;
    requires org.drombler.acp.core.action.spi;
    requires org.drombler.acp.core.commons;
    requires org.drombler.commons.docking.core;
    requires org.drombler.commons.docking.context;
    requires org.drombler.commons.client.core;
    requires org.drombler.commons.action.core;
    requires osgi.core;
    requires osgi.cmpn;
    requires org.slf4j;
    requires org.apache.commons.lang3;

}

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
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.cdi.extension.impl;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import org.drombler.acp.core.docking.ViewDocking;
import org.softsmithy.lib.util.ServiceProvider;

/**
 *
 * @author puce
 */
@ServiceProvider(serviceClass = Extension.class)
//@ApplicationScoped
public class DockingExtension implements Extension {


    public <T> void initializePropertyLoading(final @Observes ProcessInjectionTarget<T> pit) {
        AnnotatedType<T> annotatedType = pit.getAnnotatedType();
        if (annotatedType.isAnnotationPresent(ViewDocking.class)) {

        }
    }
}

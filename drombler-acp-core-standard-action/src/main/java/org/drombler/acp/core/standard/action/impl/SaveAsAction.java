/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.standard.action.impl;

//import org.drombler.acp.core.data.command.SavableAs;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.Context;

/**
 *
 * @author puce
 */
//@Action(id = "standard.saveAs", category = "core", displayName = "%saveAs.displayName")
//@MenuEntry(path = "File", position = 4205)
class SaveAsAction extends AbstractActionListener<Object> implements ActiveContextSensitive {

//    private SavableAs savableAs;
    private Context activeContext;

    public SaveAsAction() {
        setEnabled(false);
    }

    @Override
    public void onAction(Object event) {
//        savableAs.saveAs();
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
//        this.activeContext.addContextListener(SavableAs.class, (ContextEvent event) -> contextChanged());
        contextChanged();
    }

    private void contextChanged() {
//        savableAs = activeContext.find(SavableAs.class);
//        setEnabled(savableAs != null);
    }
}

package org.richclientplatform.core.standard.action.impl;

import org.richclientplatform.core.action.AbstractActionListener;
import org.richclientplatform.core.action.Action;
import org.richclientplatform.core.action.MenuEntry;
import org.richclientplatform.core.action.ToolBarEntry;
import org.richclientplatform.core.lib.util.ActiveContextSensitive;
import org.richclientplatform.core.lib.util.Context;
import org.richclientplatform.core.lib.util.ContextEvent;
import org.richclientplatform.core.lib.util.ContextListener;
import org.richclientplatform.core.standard.action.Savable;
/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

/**
 *
 * @author puce
 */
@Action(id = "standard.save", category = "core", displayName = "#save.displayName", accelerator = "Shortcut+S", icon = "save.gif")
@MenuEntry(path = "File", position = 4200)
@ToolBarEntry(toolBarId = "file", position = 50)
//@Component
//@Reference(name = "activeContextProvider", referenceInterface = ActiveContextProvider.class)
public class SaveAction extends AbstractActionListener<Object> implements ActiveContextSensitive {

    private Savable savable;
    private Context activeContext;

    public SaveAction() {
        setDisabled(true);
    }
//
//    protected void bindActiveContextProvider(ActiveContextProvider activeContextProvider) {
//        activeContext = activeContextProvider.getActiveContext();
//    }
//
//    protected void unbindActiveContextProvider(ActiveContextProvider activeContextProvider) {
//        activeContext = null;
//    }

    @Override
    public void onAction(Object event) {
        savable.save();
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
        this.activeContext.addContextListener(Savable.class, new ContextListener() {

            @Override
            public void contextChanged(ContextEvent event) {
                SaveAction.this.contextChanged();
            }
        });
        contextChanged();
    }

    private void contextChanged() {
        savable = activeContext.find(Savable.class);
        SaveAction.this.setDisabled(savable == null);
    }
}

package org.drombler.acp.core.standard.action.impl;

import org.drombler.acp.core.action.AbstractActionListener;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.acp.core.lib.util.context.ActiveContextSensitive;
import org.drombler.acp.core.lib.util.context.Context;
import org.drombler.acp.core.lib.util.context.ContextEvent;
import org.drombler.acp.core.lib.util.context.ContextListener;
import org.drombler.acp.core.standard.action.Savable;

/**
 *
 * @author puce
 */
@Action(id = "standard.save", category = "core", displayName = "#save.displayName", accelerator = "Shortcut+S", icon = "save.gif")
@MenuEntry(path = "File", position = 4200)
@ToolBarEntry(toolBarId = "file", position = 50)
public class SaveAction extends AbstractActionListener<Object> implements ActiveContextSensitive {

    private Savable savable;
    private Context activeContext;

    public SaveAction() {
        setDisabled(true);
    }

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
        setDisabled(savable == null);
    }
}

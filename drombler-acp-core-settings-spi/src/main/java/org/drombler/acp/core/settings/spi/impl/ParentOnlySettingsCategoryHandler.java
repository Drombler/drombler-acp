package org.drombler.acp.core.settings.spi.impl;

import org.drombler.acp.core.settings.jaxb.ParentOnlySettingsCategoryType;
import org.drombler.acp.core.settings.jaxb.SettingsType;
import org.drombler.acp.core.settings.spi.ParentOnlySettingsCategoryDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class ParentOnlySettingsCategoryHandler extends AbstractSettingsCategoryHandler<ParentOnlySettingsCategoryDescriptor> {

    private static final Logger LOG = LoggerFactory.getLogger(ParentOnlySettingsCategoryHandler.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindParentOnlySettingsCategoryDescriptor(ParentOnlySettingsCategoryDescriptor descriptor) {
        resolveSettingsCategoryDescriptor(descriptor);
    }

    protected void unbindParentOnlySettingsCategoryDescriptor(ParentOnlySettingsCategoryDescriptor descriptor) {
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    protected void registerSettingsType(SettingsType settings, BundleContext context) {
        settings.getParentOnlySettingsCategory().forEach(settingsCategory -> registerSettingsCategory(settingsCategory, context));
    }

    private void registerSettingsCategory(ParentOnlySettingsCategoryType settingsCategory, BundleContext context) {
        try {
            ParentOnlySettingsCategoryDescriptor settingsCategoryDescriptor = ParentOnlySettingsCategoryDescriptor.createParentOnlySettingsCategoryDescriptor(settingsCategory, context.getBundle());
            context.registerService(ParentOnlySettingsCategoryDescriptor.class, settingsCategoryDescriptor, null);
        } catch (RuntimeException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected void resolveSettingsCategoryDescriptorInitialized(ParentOnlySettingsCategoryDescriptor settingsCategoryDescriptor) {
        getApplicationThreadExecutor().execute(() -> getSettingsCategoryContainer().addCategory(settingsCategoryDescriptor));
    }
}

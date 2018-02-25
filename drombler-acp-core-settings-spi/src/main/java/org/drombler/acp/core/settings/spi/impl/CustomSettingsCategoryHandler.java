package org.drombler.acp.core.settings.spi.impl;

import org.drombler.acp.core.settings.jaxb.CustomSettingsCategoryType;
import org.drombler.acp.core.settings.jaxb.SettingsType;
import org.drombler.acp.core.settings.spi.CustomSettingsCategoryDescriptor;
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
public class CustomSettingsCategoryHandler<T> extends AbstractSettingsCategoryHandler<T, CustomSettingsCategoryDescriptor<? extends T>> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomSettingsCategoryHandler.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindCustomSettingsCategoryDescriptor(CustomSettingsCategoryDescriptor<? extends T> descriptor) {
        resolveSettingsCategoryDescriptor(descriptor);
    }

    protected void unbindCustomSettingsCategoryDescriptor(CustomSettingsCategoryDescriptor<? extends T> descriptor) {
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
        settings.getCustomSettingsCategory().forEach(settingsCategory -> registerSettingsCategory(settingsCategory, context));
    }

    private void registerSettingsCategory(CustomSettingsCategoryType settingsCategory, BundleContext context) {
        try {
            CustomSettingsCategoryDescriptor<?> settingsCategoryDescriptor = CustomSettingsCategoryDescriptor.createCustomSettingsCategoryDescriptor(settingsCategory, context.getBundle());
            context.registerService(CustomSettingsCategoryDescriptor.class, settingsCategoryDescriptor, null);
        } catch (ClassNotFoundException | RuntimeException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected void resolveSettingsCategoryDescriptorInitialized(CustomSettingsCategoryDescriptor<? extends T> settingsCategoryDescriptor) {
        getApplicationThreadExecutor().execute(() -> getSettingsCategoryContainer().addCategory(settingsCategoryDescriptor));
    }

}

package org.drombler.acp.core.settings.spi;

import static org.drombler.acp.core.commons.util.OSGiResourceBundleUtils.getPackageResourceStringPrefixed;
import org.drombler.acp.core.settings.jaxb.ParentOnlySettingsCategoryType;
import org.osgi.framework.Bundle;

public class ParentOnlySettingsCategoryDescriptor extends AbstractSettingsCategoryDescriptor {

    public ParentOnlySettingsCategoryDescriptor(String id, int position) {
        super(id, position);
    }

    public static ParentOnlySettingsCategoryDescriptor createParentOnlySettingsCategoryDescriptor(ParentOnlySettingsCategoryType settingsCategory, Bundle bundle) {
        ParentOnlySettingsCategoryDescriptor descriptor = new ParentOnlySettingsCategoryDescriptor(settingsCategory.getId(), settingsCategory.getPosition());
        descriptor.setDisplayName(getPackageResourceStringPrefixed(settingsCategory.getPackage(), settingsCategory.getDisplayName(), bundle));
        descriptor.setDisplayDescription(getPackageResourceStringPrefixed(settingsCategory.getPackage(), settingsCategory.getDisplayDescription(), bundle));
        AbstractSettingsCategoryDescriptor.configureSettingsCategoryDescriptor(descriptor, settingsCategory);
        return descriptor;
    }
}

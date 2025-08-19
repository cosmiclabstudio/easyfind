package labs.cosmic.easyfind;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import labs.cosmic.easyfind.config.EasyConfigBuilder;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EasyConfigBuilder::init;
    }
}

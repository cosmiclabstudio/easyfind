package labs.cosmic.easyfind.easyfind;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import labs.cosmic.easyfind.easyfind.config.ConfigAgent;
import labs.cosmic.easyfind.easyfind.utils.LogUtil;

public class EasyFind implements ModInitializer {
    @Override
    public void onInitialize() {
        MidnightConfig.init("easyfind", ConfigAgent.class);
        LogUtil.info("Welcome to EasyFind!");
    }
}
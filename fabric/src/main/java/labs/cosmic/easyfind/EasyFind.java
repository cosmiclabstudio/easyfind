package labs.cosmic.easyfind;

import net.fabricmc.api.ModInitializer;

public class EasyFind implements ModInitializer {
    @Override
    public void onInitialize() {
        EasyFindCommon.init();
    }
}
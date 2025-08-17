package labs.cosmic.easyfind;

import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.ItemHistory;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings({"all", "deprecation", "removal"})
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EasyFindClient {
    private final ItemHistory itemHistory = new ItemHistory(10);
    public static KeyMapping searchKey;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END) {
            while (searchKey != null && searchKey.consumeClick()) {
                if (client.player != null && client.player.isCreative()) {
                    client.setScreen(new Spotlight(itemHistory));
                }
            }
        }
    }
}
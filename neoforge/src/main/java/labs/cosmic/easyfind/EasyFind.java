package labs.cosmic.easyfind;

import com.mojang.blaze3d.platform.InputConstants;
import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.ItemHistory;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(Constants.MOD_ID)
public class EasyFind {
    private static final ItemHistory itemHistory = new ItemHistory(10);
    public static KeyMapping searchKey;

    public EasyFind(IEventBus modEventBus) {
        EasyFindCommon.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EasyFind::onClientSetup);
            NeoForge.EVENT_BUS.addListener(EasyFind::onClientTick);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        searchKey = new KeyMapping(
            "efs.spotlight",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "efs.category"
        );
        event.register(searchKey);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
    Minecraft client = Minecraft.getInstance();
        while (searchKey.consumeClick()) {
            if (client.player != null && client.player.isCreative()) {
                client.setScreen(new Spotlight(itemHistory));
            }
        }
    }
}
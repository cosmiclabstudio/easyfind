package labs.cosmic.easyfind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.lwjgl.glfw.GLFW;

@Mod(Constants.MOD_ID)
public class EasyFind {
    public static KeyMapping searchKey;

    public EasyFind(IEventBus modEventBus) {
        EasyFindCommon.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EasyFind::onClientSetup);
            modEventBus.addListener(EasyFind::onClientTick);
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
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END) {
            while (searchKey.consumeClick()) {
                if (client.player != null && client.player.isCreative()) {
                    client.setScreen(EasyFindCommon.spotlight);
                }
            }
        }

    }
}
package labs.cosmic.easyfind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings({"all", "deprecation", "removal"})
@Mod(Constants.MOD_ID)
public class EasyFind {
    public EasyFind() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EasyFindCommon.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EasyFind::onClientSetup);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(RegisterKeyMappingsEvent event) {
        EasyFindClient.searchKey = new KeyMapping(
            "efs.spotlight",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "efs.category"
        );
        event.register(EasyFindClient.searchKey);
    }
}
package labs.cosmic.easyfind;

import com.mojang.blaze3d.platform.InputConstants;
import labs.cosmic.easyfind.config.EasyConfigBuilder;
import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.ItemHistory;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings({"all", "deprecation", "removal"})
@Mod(Constants.MOD_ID)
public class EasyFind {
    private static final ItemHistory itemHistory = new ItemHistory(10);
    public static KeyMapping searchKey;

    public EasyFind() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EasyFindCommon.init();

        ModLoadingContext.get().registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (client, parent) -> EasyConfigBuilder.init(parent)));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(EasyFind::onClientSetup);
            MinecraftForge.EVENT_BUS.addListener(EasyFind::clientTick);
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
    public static void clientTick(TickEvent.ClientTickEvent event) {
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
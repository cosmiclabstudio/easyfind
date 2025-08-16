package labs.cosmic.easyfind;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class EasyFindClient implements ClientModInitializer {
    private static KeyMapping openEFS;

    @Override
    public void onInitializeClient() {
        openEFS = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "efs.spotlight",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_GRAVE_ACCENT,
            "efs.category"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openEFS.isDown()) {
                if (client.player != null && client.player.isCreative()) {
                    client.setScreen(EasyFindCommon.spotlight);
                }
            }
        });
    }
}

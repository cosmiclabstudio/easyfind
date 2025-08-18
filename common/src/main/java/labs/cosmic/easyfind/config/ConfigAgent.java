package labs.cosmic.easyfind.config;

// Temporary disable config so I can test out first.

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

public class ConfigAgent {
    public static ConfigClassHandler<ConfigAgent> HANDLER = ConfigClassHandler.createBuilder(ConfigAgent.class)
        .id(new ResourceLocation("easyfind", "settings"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
            .setJson5(true)
            .build())
        .build();

    // Cosmetics
    @SerialEntry
    public static boolean darkenBG = true;
    @SerialEntry
    public static boolean showDescription = true;
    @SerialEntry
    public static boolean coloredRarity = true;

    // Behaviour
    @SerialEntry
    public static ReplaceNeighbor replaceNeighbor = ReplaceNeighbor.CURRENT;
    @SerialEntry
    public static KeepScreen keepScreenOn = KeepScreen.SHIFT;
    @SerialEntry
    public static boolean forcedReplace = false;
    @SerialEntry
    public static boolean ignoreExisting = false;
    @SerialEntry
    public static boolean saveHistory = true;
    @SerialEntry
    public static boolean showDisabledItem;

    public enum ReplaceNeighbor {CURRENT, NEXT, PREVIOUS}
    public enum KeepScreen {SHIFT, ALWAYS}
}

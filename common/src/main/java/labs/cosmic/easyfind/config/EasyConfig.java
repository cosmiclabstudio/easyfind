package labs.cosmic.easyfind.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;

public class EasyConfig {
    public static ConfigClassHandler<EasyConfig> HANDLER = ConfigClassHandler.createBuilder(EasyConfig.class)
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(YACLPlatform.getConfigDir().resolve("easyfind.json"))
            .build())
        .build();

    // Cosmetics
    @SerialEntry(comment = "Darkened Background")
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
    public static boolean showDisabledItem = false;

    public enum ReplaceNeighbor {CURRENT, NEXT, PREVIOUS}
    public enum KeepScreen {SHIFT, ALWAYS}
}

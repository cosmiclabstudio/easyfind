package labs.cosmic.easyfind.config;

// Temporary disable config so I can test out first.

public class ConfigAgent {
    public enum ReplaceNeighbor { CURRENT, NEXT, PREVIOUS }
    public enum KeepScreen { SHIFT, ALWAYS }
    
    // Cosmetics
    public static boolean darkenBG = true;
    public static boolean showDescription = true;
    public static boolean coloredRarity = true;
    
    // Behaviour
    public static ReplaceNeighbor replaceNeighbor = ReplaceNeighbor.CURRENT;
    public static KeepScreen keepScreenOn = KeepScreen.SHIFT;
    public static boolean forcedReplace = false;
    public static boolean ignoreExisting = false;
    
    public static boolean saveHistory = true;
    public static boolean showDisabledItem;
}

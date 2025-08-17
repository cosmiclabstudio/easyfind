package labs.cosmic.easyfind;

import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.ItemHistory;

public class EasyFindCommon {
    private static final ItemHistory itemHistory = new ItemHistory(24);
    public static Spotlight spotlight = new Spotlight(itemHistory);

    public static void init() {

    }
}
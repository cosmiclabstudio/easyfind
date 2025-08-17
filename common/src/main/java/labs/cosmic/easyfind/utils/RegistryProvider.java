package labs.cosmic.easyfind.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RegistryProvider {
    private static final Collection<Item> ITEMS;
    private static final List<Item> BLACKLISTED_ITEMS = List.of(
        Items.AIR
    );

    static {
        List<Item> items = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (BLACKLISTED_ITEMS.contains(item)) continue;
            items.add(item);
        }
        ITEMS = Collections.unmodifiableCollection(items);
    }

    public static Collection<Item> getItems() {
        return ITEMS;
    }
}

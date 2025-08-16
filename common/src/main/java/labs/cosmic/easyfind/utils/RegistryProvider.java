package labs.cosmic.easyfind.utils;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RegistryProvider {
    private static final Collection<Item> ITEMS;
    private static final Collection<Item> BLACKLISTED_ITEMS = List.of(
            Registries.ITEM.get(Identifier.tryParse("minecraft:air"))
    );
    
    static {
        List<Item> items = new ArrayList<>();
        for (Item item : Registries.ITEM) {
            if (BLACKLISTED_ITEMS.contains(item)) continue;
            items.add(item);
        }
        ITEMS = Collections.unmodifiableCollection(items);
    }

    public static Collection<Item> getItems() {
        return ITEMS;
    }
}

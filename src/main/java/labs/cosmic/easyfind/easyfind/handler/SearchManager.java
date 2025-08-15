package labs.cosmic.easyfind.easyfind.handler;

import labs.cosmic.easyfind.easyfind.utils.FuzzyFind;
import labs.cosmic.easyfind.easyfind.utils.RegistryProvider;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import net.minecraft.item.Item;
import java.util.List;

public class SearchManager {
    public List<Item> search(String query) {
        if (query.isEmpty()) {
            return List.of();
        }
        return FuzzyFind.search(RegistryProvider.getItems(), query)
                .stream()
                .map(BoundExtractedResult::getReferent)
                .toList();
    }
}

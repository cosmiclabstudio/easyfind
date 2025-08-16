package labs.cosmic.easyfind.handler;

import labs.cosmic.easyfind.utils.RegistryProvider;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import net.minecraft.world.item.Item;

import java.util.List;

public class SearchHandler {
    public List<Item> search(String query) {
        if (query.isEmpty()) {
            return List.of();
        }

        String[] subQueries = query.split("\\|");
        java.util.Map<Item, Integer> itemToScore = new java.util.HashMap<>();
        for (String subQuery : subQueries) {
            subQuery = subQuery.trim();
            if (subQuery.isEmpty()) continue;
            for (var result : searchSingleQueryScored(subQuery)) {
                Item item = result.getReferent();
                int score = result.getScore();
                itemToScore.merge(item, score, Math::max);
            }
        }

        return itemToScore.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .map(java.util.Map.Entry::getKey)
            .toList();
    }

    // Helper for a single search query (mod/tag/search terms), returns scored results
    private List<BoundExtractedResult<Item>> searchSingleQueryScored(String query) {
        String[] words = query.split("\\s+");
        List<String> modNames = new java.util.ArrayList<>();
        List<String> tagNames = new java.util.ArrayList<>();
        List<String> searchTerms = new java.util.ArrayList<>();
        for (String word : words) {
            if (word.startsWith("@")) {
                modNames.add(word.substring(1));
            } else if (word.startsWith("#")) {
                tagNames.add(word.substring(1));
            } else {
                searchTerms.add(word);
            }
        }

        // TODO: Move away from builtInRegistryHolder sometime soon
        String filteredQuery = String.join(" ", searchTerms);
        List<Item> modFilteredItems = RegistryProvider.getItems().stream()
            .filter(item -> modNames.isEmpty() || modNames.stream().anyMatch(mod -> {
                var key = item.builtInRegistryHolder().key().location();
                return key.getNamespace().startsWith(mod);
            }))
            .toList();
        List<Item> tagFilteredItems = modFilteredItems.stream()
            .filter(item -> tagNames.isEmpty() || tagNames.stream().anyMatch(
                tag -> item.builtInRegistryHolder().tags().anyMatch(
                    tagKey -> tagKey.location().getPath().startsWith(tag))))
            .toList();

        if (filteredQuery.isBlank() && (!modNames.isEmpty() || !tagNames.isEmpty())) {
            return tagFilteredItems.stream()
                .map(item -> new BoundExtractedResult<>(item, item.toString(), 100, 0))
                .toList();
        }

        return FuzzySearch.extractTop(
            filteredQuery,
            tagFilteredItems,
            Item::toString,
            24,
            50
        );
    }
}

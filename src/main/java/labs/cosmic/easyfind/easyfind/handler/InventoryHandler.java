package labs.cosmic.easyfind.easyfind.handler;

import labs.cosmic.easyfind.easyfind.config.ConfigAgent;
import labs.cosmic.easyfind.easyfind.utils.ItemHistory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.sound.SoundEvents;

public class InventoryHandler {
    private final ItemHistory itemHistory;

    public InventoryHandler(ItemHistory itemHistory) {
        this.itemHistory = itemHistory;
    }

    public void giveItem(MinecraftClient client, Item item) {
        ClientPlayerEntity player = client.player;
        if (player == null || !player.getAbilities().creativeMode) return;
        if (!player.networkHandler.hasFeature(item.getRequiredFeatures())) return;

        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = new ItemStack(item);
        float audioPitch = ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f;

        if (ConfigAgent.saveHistory) itemHistory.push(item);

        int slot = inventory.selectedSlot;
        if (!ConfigAgent.ignoreExisting) {
            for (int i = 0; i <= 8; i++) {
                if (inventory.main.get(i).isOf(item)) {
                    inventory.selectedSlot = i;
                    return;
                }
            }
        }
        int emptySlot = inventory.getEmptySlot();
        if (ConfigAgent.forcedReplace) slot = inventory.selectedSlot;
        else if (emptySlot == -1 || emptySlot > 8) {
            slot = inventory.selectedSlot;
            slot = switch (ConfigAgent.replaceNeighbor) {
                case CURRENT -> slot;
                case NEXT -> slot + 1;
                case PREVIOUS -> slot - 1;
            };
            if (ConfigAgent.replaceNeighbor != ConfigAgent.ReplaceNeighbor.CURRENT)
                slot = switch (slot) {
                    case -1 -> 8;
                    case 9 -> 0;
                    default -> slot;
                };
        } else slot = emptySlot;

        player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(slot + 36, itemStack));
        inventory.selectedSlot = slot;
        player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2f, audioPitch);
    }
}

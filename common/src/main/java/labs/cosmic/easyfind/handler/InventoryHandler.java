package labs.cosmic.easyfind.handler;

import labs.cosmic.easyfind.config.ConfigAgent;
import labs.cosmic.easyfind.utils.ItemHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryHandler {
    private final ItemHistory itemHistory;

    public InventoryHandler(ItemHistory itemHistory) {
        this.itemHistory = itemHistory;
    }

    public void giveItem(Minecraft minecraft, Item item) {
        LocalPlayer player = minecraft.player;
        if (player == null || !player.getAbilities().instabuild) return;

        Inventory inventory = player.getInventory();
        ItemStack itemStack = new ItemStack(item);
        float audioPitch = ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f;

        if (ConfigAgent.saveHistory) itemHistory.push(item);

        int slot = inventory.selected;
        if (!ConfigAgent.ignoreExisting) {
            for (int i = 0; i <= 8; i++) {
                if (inventory.getItem(i).is(item)) {
                    inventory.selected = i;
                    return;
                }
            }
        }
        // Find first empty slot manually
        int emptySlot = -1;
        for (int i = 0; i <= 8; i++) {
            if (inventory.getItem(i).isEmpty()) {
                emptySlot = i;
                break;
            }
        }
        if (ConfigAgent.forcedReplace) slot = inventory.selected;
        else if (emptySlot == -1) {
            slot = inventory.selected;
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

        player.connection.send(new ServerboundSetCreativeModeSlotPacket(slot + 36, itemStack));
        inventory.selected = slot;
        player.playSound(SoundEvents.ITEM_PICKUP, 0.2f, audioPitch);
    }
}

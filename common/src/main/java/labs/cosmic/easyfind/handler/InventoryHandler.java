package labs.cosmic.easyfind.handler;

import labs.cosmic.easyfind.config.ConfigAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryHandler {
    private static Inventory inventory;
    public InventoryHandler() {}

    public static void giveItem(Item item) {
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        inventory = player.getInventory();

        if (!player.getAbilities().instabuild) return;

        ItemStack itemStack = new ItemStack(item);
        float audioPitch = ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f;

        int slot;
        if (!ConfigAgent.ignoreExisting) {
            for (int i = 0; i <= 8; i++) {
                if (inventory.getItem(i).is(item)) {
                    setSlot(i);
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
        if (ConfigAgent.forcedReplace) slot = getSlot();
        else if (emptySlot == -1) {
            slot = getSlot();
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

        // I'm very confused
        player.connection.send(new ServerboundSetCreativeModeSlotPacket(slot + 36, itemStack));
        player.inventoryMenu.setItem(slot + 36, 0, itemStack);
        player.inventoryMenu.broadcastChanges();

        setSlot(slot);
        player.playSound(SoundEvents.ITEM_PICKUP, 0.2f, audioPitch);
    }

    // function split for easier version managing
    private static int getSlot() {
        //? if <1.21.5 {
        return inventory.selected;
        //?} elif >1.21.5 {
        /*return inventory.getSelectedSlot();
        *///?}
    }

    private static void setSlot(int slot) {
        //? if <1.21.5 {
        inventory.selected = slot;
        //?} elif >1.21.5 {
        /*inventory.setSelectedSlot(slot);
        *///?}
    }
}
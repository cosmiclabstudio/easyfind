package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.config.ConfigAgent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ResultWidget extends ObjectSelectionList.Entry<ResultWidget> {
    private final Font font;
    private final Item item;
    private final boolean isEnabled;

    public ResultWidget(final Font font, final Item item, boolean isEnabled) {
        this.font = font;
        this.item = item;
        this.isEnabled = isEnabled;
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.translatable(this.item.getDescriptionId());
    }
    
    public Item getItem() {
        return this.item;
    }

    @Override
    public void render(@NotNull GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        final Component text;
        final ItemStack itemStack = new ItemStack(this.item);
        final Rarity rarity = itemStack.getRarity();
        Component meta = Component.translatable(item.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY);
        if (isEnabled) {
            int color = ConfigAgent.coloredRarity ? rarity.color.getId() : 0xFFFFFF;
            text = Component.translatable(item.getDescriptionId()).withStyle(Objects.requireNonNull(ChatFormatting.getById(color)));
        } else {
            text = Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.STRIKETHROUGH, ChatFormatting.GRAY);
            meta = Component.translatable("item.disabled").withStyle(ChatFormatting.RED);
        }
        // Render item using ItemRenderer
        context.renderItem(itemStack, x + 2, y + 2);
        if (ConfigAgent.showDescription && !meta.getString().contains(".desc")) {
            context.drawString(this.font, text, x + 22, y + 1, 0xFFFFFF, false);
            context.drawString(this.font, meta, x + 22, y + 12, 0xAAAAAA, false);
        } else {
            context.drawString(this.font, text, x + 22, y + 6, 0xFFFFFF, false);
        }
    }
}

package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.config.ConfigAgent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class ResultWidget extends ObjectSelectionList.Entry<ResultWidget> {
    private final Font font;
    private final Item item;
    private final boolean isEnabled;

    public ResultWidget(final Item item, boolean isEnabled) {
        this.font = Minecraft.getInstance().font;
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
    public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        final Component text;
        final ItemStack itemStack = new ItemStack(this.item);
        final Rarity rarity = itemStack.getRarity();
        Component meta = Component.translatable(item.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY);
        if (isEnabled) {
            int color = ConfigAgent.coloredRarity ?
                /*? =1.20.1 {*/ rarity.color.getId() /*?} >=1.21.1 {*/ /*rarity.color().getId() *//*?}*/
                : 0xFFFFFF;
            text = Component.translatable(item.getDescriptionId()).withStyle(Objects.requireNonNull(ChatFormatting.getById(color)));
        } else {
            text = Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.STRIKETHROUGH, ChatFormatting.GRAY);
            meta = Component.translatable("item.disabled").withStyle(ChatFormatting.RED);
        }
        // Render item using ItemRenderer
        context.renderItem(itemStack, x + 2, y + 2);
        if (ConfigAgent.showDescription && !meta.getString().contains(".desc")) {
            context.drawString(this.font, text, x + 22, y + 1, Color.WHITE.getRGB(), false);
            context.drawString(this.font, meta, x + 22, y + 12, Color.GRAY.getRGB(), false);
        } else {
            context.drawString(this.font, text, x + 22, y + 6, Color.WHITE.getRGB(), false);
        }
    }
}

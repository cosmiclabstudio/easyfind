package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.screens.Spotlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ResultListWidget extends ObjectSelectionList<ResultWidget> {
    final private Spotlight spotlight;
    final private int entryWidth;
    final private Minecraft minecraft;
    final private Font font;

    public ResultListWidget(Spotlight screen, 
                            Minecraft minecraft,
                            int width,
                            int height, 
                            int top,
                            int bottom) {
        super(minecraft, width, height, top, bottom, 24);
        this.spotlight = screen;
        this.entryWidth = width;
        this.minecraft = minecraft;
        this.font = minecraft.font;
    }

    public void selectNextEntryInDirection(final ScreenDirection direction) {
        // AbstractSelectionList does not have getNeighboringEntry, so use built-in navigation
        int idx = this.children().indexOf(this.getSelected());
        int nextIdx = direction == ScreenDirection.DOWN ? idx + 1 : idx - 1;
        if (nextIdx >= 0 && nextIdx < this.children().size()) {
            this.setSelected(this.children().get(nextIdx));
        }
    }
    
    @Override
    public int getRowWidth() {
        return this.entryWidth - 34;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowRight() - 6;
    }
    
    @Override
    public void render(final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {
        int x = this.x0;
        int y = this.y0;
        int width = this.width;
        int height = this.height;
        // no history found
        if (spotlight.getSearchboxWidget().getValue().isEmpty() && spotlight.getItemHistory().isEmpty()) return;
        // avoid displaying not found on blank search query
        if (spotlight.getSearchboxWidget().getValue().isEmpty() && this.children().isEmpty()) return;
        context.fill(x, y, x + width, y + height, Color.BLACK.getRGB()); // background
        // Draw border manually
        context.fill(x - 1, y - 1, x + width + 1, y, Color.WHITE.getRGB()); // top
        context.fill(x - 1, y + height, x + width + 1, y + height + 1, Color.WHITE.getRGB()); // bottom
        context.fill(x - 1, y, x, y + height, Color.WHITE.getRGB()); // left
        context.fill(x + width, y, x + width + 1, y + height, Color.WHITE.getRGB()); // right
        if (this.children().isEmpty()) {
            final Component text = Component.translatable("efs.404");
            int textWidth = this.font.width(text);
            context.drawString(this.font, text, x + width / 2 - textWidth / 2, y + height / 2 - 5, Color.PINK.getRGB(), true);
        }
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    protected void renderItem(GuiGraphics context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        super.renderItem(context, mouseX, mouseY, delta, index, x - 14, y, entryWidth, entryHeight);
    }
    
    @Override
    protected void renderSelection(GuiGraphics context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        int i = this.x0 + (this.width - entryWidth) / 2 - 14;
        int j = this.x0 + (this.width + entryWidth) / 2 + ((this.getMaxScroll() > 0) ? 8 : 14);
        context.fill(i, y - 2, j, y + entryHeight + 2, borderColor);
        context.fill(i + 1, y - 1, j - 1, y + entryHeight + 1, fillColor);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // mouse click helper
    public int getEntryY(final double mouseY) {
        return (int) (mouseY - this.y0) - this.headerHeight + (int) this.getScrollAmount() - 2;
    }

    public int getEntryHeight() {
        return this.itemHeight;
    }

    public ResultWidget at(final int n) {
        if (n < 0 || n >= this.children().size()) {
            return null;
        }
        return this.children().get(n);
    }

    public boolean isMouseOver(final double mouseX, final double mouseY) {
        int x = this.x0;
        int y = this.y0;
        int width = this.width;
        int height = this.height;
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            spotlight.giveItem();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || isPrintableSymbol(keyCode)) {
            spotlight.setFocused(spotlight.getSearchboxWidget());
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void setFocused(boolean focused) {
        super.setFocused(false);
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        // Not on priority, sorry.
    }

    private boolean isPrintableSymbol(int keyCode) {
        return (keyCode >= 33 && keyCode <= 47) || (keyCode >= 58 && keyCode <= 64) ||
            (keyCode >= 91 && keyCode <= 96) || (keyCode >= 123 && keyCode <= 126);
    }
}

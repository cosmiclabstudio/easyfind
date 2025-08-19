package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.screens.Spotlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.inventory.CreativeInventoryListener;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.awt.*;
//? if >=1.21.1 {
/*import com.mojang.blaze3d.systems.RenderSystem;
*///?}

public class ResultListWidget extends ObjectSelectionList<ResultWidget> {
    final private Spotlight spotlight;
    final private int entryWidth;
    final private Font font;
    final private int left;
    final private int top;


    public ResultListWidget(Spotlight screen,
                            Minecraft client,
                            int left,
                            int width,
                            int height,
                            int top,
                            int bottom) {
        //? if =1.20.1 {
        super(client, width, height, top, bottom, 24);
        //?} elif >=1.21.1 {
        /*super(client, width, height, top, 24);
        *///?}
        this.spotlight = screen;
        this.entryWidth = width;
        this.left = left;
        this.top = top;
        this.font = client.font;
        //? if =1.20.1 {
        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
        //?}

        assert client.player != null;
        //? if <1.21.5 {
        if (client.gameMode.hasInfiniteItems()) {
        //?} elif >=1.21.5 {
        /*if (client.player.hasInfiniteMaterials()) {
        *///?}
            client.player.inventoryMenu.addSlotListener(new CreativeInventoryListener(this.minecraft));
        }
    }

    public void selectNextEntryInDirection(final ScreenDirection direction) {
        int idx = this.children().indexOf(this.getSelected());
        int nextIdx = direction == ScreenDirection.DOWN ? idx + 1 : idx - 1;
        if (nextIdx >= 0 && nextIdx < this.children().size()) {
            this.setSelected(this.children().get(nextIdx));
        }
    }

    //? if >=1.21.1 {
    /*@Override
    protected void renderListSeparators(GuiGraphics context) {
        // shit just gone lmao
        //? if <=1.21.5 {
        RenderSystem.enableBlend();
         //?}
        context.renderOutline(this.getX(), this.getY() - 1, this.getWidth(), this.getHeight() + 2, Color.WHITE.getRGB()); // border
        //? if <=1.21.5 {
        RenderSystem.disableBlend();
         //?}
    }
    *///?}

    @Override
    public int getRowWidth() {
        return this.entryWidth;
    }

    @Override
        //? if <1.21.5 {
    protected int getScrollbarPosition() {
        //?} elif >=1.21.5 {
    /*protected int scrollBarX() {
        *///?}
        return this.getRowRight() - /*? =1.20.1 {*/ 8 /*?} >=1.21.1 {*/ /*9 *//*?}*/;
    }

    @Override
        //? if =1.20.1 {
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        //?} elif >=1.21.1 {
    /*public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        *///?}
        int left = this.left;
        int top = this.top;
        int right = this.left + this.width;
        int bottom = this.top + this.height;
        int width = this.width;
        int height = this.height;

        // no history found
        if (spotlight.getSearchboxWidget().getValue().isEmpty() && spotlight.getItemHistory().isEmpty()) return;
        // avoid displaying not found on blank search query
        if (spotlight.getSearchboxWidget().getValue().isEmpty() && this.children().isEmpty()) return;

        context.fill(left, top, right, bottom + 1, Color.BLACK.getRGB()); // background

        //Draw border manually on 1.20.1
        //? if =1.20.1 {
        context.fill(left - 1, top - 1, right + 1, top, Color.WHITE.getRGB()); // top
        context.fill(left - 1, bottom, right + 1, bottom + 1, Color.WHITE.getRGB()); // bottom
        context.fill(left - 1, top, left, bottom, Color.WHITE.getRGB()); // left
        context.fill(right, top, right + 1, bottom, Color.WHITE.getRGB()); // right
        //?}
        if (this.children().isEmpty()) {
            final Component text = Component.translatable("efs.404");
            int textWidth = this.font.width(text);
            context.drawString(this.font, text, left + (width / 2) - (textWidth / 2), top + (height / 2) - 5, Color.PINK.getRGB(), true);
        }
        //? if =1.20.1 {
        super.render(context, mouseX, mouseY, delta);
        //?} elif >=1.21.1 {
        /*super.renderWidget(context, mouseX, mouseY, delta);
        *///?}
    }

    @Override
    protected void renderItem(GuiGraphics context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        super.renderItem(context, mouseX, mouseY, delta, index, this.left + 4, y, entryWidth, entryHeight);
    }

    @Override
    protected void renderSelection(GuiGraphics context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        int i = this.left + (this.width - entryWidth) / 2 + 2;
        //? if =1.20.1 {
        int j = this.left + (this.width + entryWidth) / 2 - ((this.getMaxScroll() > 0) ? 8 : 2);
        //?} elif <1.21.5 {
        /*int j = this.left + (this.width + entryWidth) / 2 - ((this.getMaxScroll() > 0) ? 9 : 2);
        *///?} elif >=1.21.5 {
        /*int j = this.left + (this.width + entryWidth) / 2 - ((this.maxScrollAmount() > 0) ? 9 : 2);
        *///?}

        context.fill(i, y - 2, j, y + entryHeight + 2, borderColor);
        context.fill(i + 1, y - 1, j - 1, y + entryHeight + 1, fillColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // mouse click helper
    public int getEntryY(final double mouseY) {
        return (int) (mouseY - this.top) - this.headerHeight + (int) /*? <1.21.5 {*/ this.getScrollAmount() /*?} >=1.21.5 {*/ /*this.scrollAmount() *//*?}*/ - 2;
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
        int x = this.left;
        int y = this.top;
        int width = this.width;
        int height = this.height;
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            ResultWidget selected = this.getSelected();
            if (selected != null) {
                spotlight.giveItem(selected.getItem());
                return true;
            }
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || isPrintableSymbol(keyCode)) {
            SearchboxWidget searchbox = spotlight.getSearchboxWidget();
            spotlight.setFocused(searchbox);
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                searchbox.moveCursorToEnd(/*? >1.21 {*//*false*//*?}*/);
                searchbox.setHighlightPos(0);
            } else if (isPrintableSymbol(keyCode)) {
                searchbox.keyPressed(keyCode, 0, 0);
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(false);
    }

    private boolean isPrintableSymbol(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) return true;
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) return true;
        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) return true;
        if (keyCode >= GLFW.GLFW_KEY_KP_0 && keyCode <= GLFW.GLFW_KEY_KP_9) return true;
        return (keyCode >= 44 && keyCode <= 57) || (keyCode >= 59 && keyCode <= 93) || (keyCode >= 96 && keyCode <= 122);
    }
}

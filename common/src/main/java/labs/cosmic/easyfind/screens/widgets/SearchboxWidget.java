package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.SearchResult;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

public class SearchboxWidget extends EditBox {
    private final Spotlight spotlight;
    private final Font font;
    private final Component placeholder;
    private BiConsumer<SearchResult, ResultWidget> resultConsumer = null;
    
    public SearchboxWidget(Spotlight screen, Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Component.translatable("efs.title"));
        this.spotlight = screen;
        this.font = font;
        this.placeholder = Component.translatable("efs.placeholder").withStyle(ChatFormatting.GRAY);
        this.setMaxLength(256);
        this.setEditable(true);
        // Placeholder logic will be handled in render
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Move selection down on key down
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.spotlight.getResultList().selectNextEntryInDirection(ScreenDirection.DOWN);
        }
        final SearchResult result = SearchResult.fromKeyCode(keyCode);
        if (result != null) {
            final ResultWidget resultWidgetEntry = this.spotlight.getResultList().getSelected();
            if (resultWidgetEntry != null) {
                this.resultConsumer.accept(result, resultWidgetEntry);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        if (this.placeholder != null && this.getValue().isEmpty()) {
            graphics.drawString(
                this.font,
                this.placeholder,
                this.getX() + 4,
                this.getY() + (this.getHeight() - 8) / 2,
                0xE0E0E0
            );
        }
    }

    public void setResultConsumer(BiConsumer<SearchResult, ResultWidget> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }
}

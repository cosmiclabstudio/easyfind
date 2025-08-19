package labs.cosmic.easyfind.screens.widgets;

import labs.cosmic.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.utils.SearchResult;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class SearchboxWidget extends EditBox {
    private final Spotlight spotlight;
    private final Font font;
    private final Component placeholder;

    public SearchboxWidget(Spotlight screen, Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Component.translatable("efs.title"));
        this.spotlight = screen;
        this.font = font;
        this.placeholder = Component.translatable("efs.placeholder").withStyle(ChatFormatting.GRAY);

        this.setMaxLength(256);
        this.setEditable(true);
        this.setCanLoseFocus(true);
        this.setTextColor(-1);
        this.setTextColorUneditable(-1);
        this.setFormatter((text, cursorPos) -> {
            MutableComponent result = Component.literal("");
            int i = 0;
            while (i < text.length()) {
                int start = i;
                int end;
                int color = -1;
                switch (text.charAt(i)) {
                    case '@':
                        color = 0xFF69B4; // pink
                        end = i + 1;
                        while (end < text.length() && !Character.isWhitespace(text.charAt(end)) && text.charAt(end) != '@' && text.charAt(end) != '#')
                            end++;
                        break;
                    case '#':
                        color = 0x90EE90; // light green
                        end = i + 1;
                        while (end < text.length() && !Character.isWhitespace(text.charAt(end)) && text.charAt(end) != '@' && text.charAt(end) != '#')
                            end++;
                        break;
                    case '|':
                        color = 0xADD8E6; // light blue
                        end = i + 1;
                        break;
                    default:
                        end = i;
                        while (end < text.length() && text.charAt(end) != '@' && text.charAt(end) != '#' && text.charAt(end) != '|')
                            end++;
                        break;
                }
                String token = text.substring(start, end);
                final int tokenColor = color;
                if (tokenColor != -1) {
                    result.append(Component.literal(token).withStyle(style -> style.withColor(tokenColor)));
                } else {
                    result.append(Component.literal(token));
                }
                i = end;
            }
            return result.getVisualOrderText();
        });
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
                spotlight.giveItem(resultWidgetEntry.getItem());
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
        //? if =1.20.1 {
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        //?} elif >=1.21.1 {
    /*public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(graphics, mouseX, mouseY, delta);
        *///?}
        if (this.placeholder != null && this.getValue().isEmpty()) {
            graphics.drawString(
                this.font,
                this.placeholder,
                this.getX() + 4,
                this.getY() + (this.getHeight() - 8) / 2,
                Color.GRAY.getRGB()
            );
        }
    }
}

package labs.cosmic.easyfind.easyfind.screens.widgets;

import labs.cosmic.easyfind.easyfind.screens.Spotlight;
import labs.cosmic.easyfind.easyfind.utils.SearchResult;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.function.BiConsumer;

public class SearchboxWidget extends TextFieldWidget {
    private final Spotlight spotlight;
    private final TextRenderer textRenderer;
    private final Text placeholder;
    private BiConsumer<SearchResult, ResultWidget> resultConsumer = null;
    
    public SearchboxWidget(Spotlight screen, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y - 4, width, height + 4, Text.translatable("efs.title"));
        this.spotlight = screen;
        this.textRenderer = textRenderer;
        this.placeholder = Text.translatable("efs.placeholder").formatted(Formatting.GRAY);
        this.setMaxLength(256);
        this.setFocusUnlocked(true);
        this.setEditableColor(-1);
        this.setUneditableColor(-1);
        this.setPlaceholder(this.placeholder);
        this.setRenderTextProvider((text, cursorPos) -> {
            MutableText result = Text.literal("");
            int i = 0;
            while (i < text.length()) {
                int start = i;
                int end = i;
                int color = -1;
                switch (text.charAt(i)) {
                    case '@':
                        color = 0xFF69B4; // pink
                        end = i + 1;
                        while (end < text.length() && !Character.isWhitespace(text.charAt(end)) && text.charAt(end) != '@' && text.charAt(end) != '#') end++;
                        break;
                    case '#':
                        color = 0x90EE90; // light green
                        end = i + 1;
                        while (end < text.length() && !Character.isWhitespace(text.charAt(end)) && text.charAt(end) != '@' && text.charAt(end) != '#') end++;
                        break;
                    case '|':
                        color = 0xADD8E6; // light blue
                        end = i + 1;
                        break;
                    default:
                        end = i;
                        while (end < text.length() && text.charAt(end) != '@' && text.charAt(end) != '#' && text.charAt(end) != '|') end++;
                        break;
                }
                String token = text.substring(start, end);
                final int tokenColor = color;
                if (tokenColor != -1) {
                    result.append(Text.literal(token).styled(style -> style.withColor(tokenColor)));
                } else {
                    result.append(Text.literal(token));
                }
                i = end;
            }
            return result.asOrderedText();
        });
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Get to the second entry instead on keydown
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            this.spotlight.getResultList().selectNextEntryInDirection(NavigationDirection.DOWN);
        }
        
        final SearchResult result = SearchResult.fromKeyCode(keyCode);
        if (result != null) {
            final ResultWidget resultWidgetEntry = this.spotlight.getResultList().getSelectedOrNull();
            if (resultWidgetEntry != null) {
                this.resultConsumer.accept(result, resultWidgetEntry);
            }
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.placeholder != null && this.getText().isEmpty()) {
            context.drawTextWithShadow(
                    this.textRenderer, 
                    this.placeholder, 
                    this.getX() + 4, 
                    this.getY() + (this.height - 8) / 2, 
                    0xE0E0E0
            );
        }
    }

    public void setResultConsumer(BiConsumer<SearchResult, ResultWidget> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }
}

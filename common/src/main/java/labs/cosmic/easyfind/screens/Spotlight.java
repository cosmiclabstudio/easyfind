package labs.cosmic.easyfind.screens;

import labs.cosmic.easyfind.config.ConfigAgent;
import labs.cosmic.easyfind.handler.InventoryHandler;
import labs.cosmic.easyfind.handler.SearchHandler;
import labs.cosmic.easyfind.screens.widgets.ResultListWidget;
import labs.cosmic.easyfind.screens.widgets.ResultWidget;
import labs.cosmic.easyfind.screens.widgets.SearchboxWidget;
import labs.cosmic.easyfind.utils.ItemHistory;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.lwjgl.glfw.GLFW;

import java.util.Queue;
import java.util.function.BiConsumer;

public class Spotlight extends Screen {
    private static int slot;
    final int inputHeight = 16;
    private final ItemHistory itemHistory;
    private final SearchHandler searchManager;
    private final InventoryHandler inventoryHandler;
    LocalPlayer player;
    private SearchboxWidget searchboxWidget;
    private ResultListWidget resultListWidget;
    private String prevQuery;
    private Item lastClickItemEntry;
    private boolean isShiftDown = false;
    private long lastClickTime;

    public Spotlight(ItemHistory itemHistory) {
        super(Component.nullToEmpty(I18n.get("efs.title")));
        this.itemHistory = itemHistory;
        this.searchManager = new SearchHandler();
        this.inventoryHandler = new InventoryHandler(itemHistory);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        Minecraft minecraft = Minecraft.getInstance();
        player = minecraft.player;

        final int resultBoxWidth = Math.min(super.width / 2, 300);
        final int resultBoxHeight = Math.min(super.height / 2, 300);
        final int searchFieldX = super.width / 2 - resultBoxWidth / 2;
        final int searchFieldY = (super.height / 6) + 6;
        final int resultBoxX = super.width / 2 - resultBoxWidth / 2;
        final int resultBoxY = (super.height / 6) + inputHeight + 6;

        this.searchboxWidget = new SearchboxWidget(
            this,
            minecraft.font,
            searchFieldX,
            searchFieldY,
            resultBoxWidth,
            inputHeight
        );

        this.resultListWidget = new ResultListWidget(
            this,
            minecraft,
            resultBoxX,
            resultBoxWidth,
            resultBoxHeight,
            resultBoxY + 1,
            resultBoxY + resultBoxHeight
        );

        this.resultListWidget.setLeftPos(resultBoxX);
        this.searchboxWidget.setFocused(true);
        this.searchboxWidget.setEditable(true);
        this.searchboxWidget.setVisible(true);

        super.addRenderableWidget(this.searchboxWidget);
        super.addRenderableWidget(this.resultListWidget);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (ConfigAgent.darkenBG) this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            this.updateResults();
            if (!minecraft.player.getAbilities().instabuild) this.onClose();
        }
    }

    private void search(final String query) {
        if (this.prevQuery != null && this.prevQuery.equals(query)) {
            return;
        }
        this.prevQuery = query;
        this.resultListWidget.children().clear();

        if (query.isEmpty()) {
            if (!ConfigAgent.saveHistory) return;
            this.itemHistory.getItemHistory().forEach(this::addToResult);
        } else {
            searchManager.search(query).forEach(this::addToResult);
        }

        if (!resultListWidget.children().isEmpty()) {
            resultListWidget.setSelected(resultListWidget.children().get(0));
        } else {
            resultListWidget.setSelected(null);
        }
        resultListWidget.setScrollAmount(0);
    }

    private void addToResult(Item item) {
        boolean hasFeature = true; // Feature check removed for now
        resultListWidget.children().add(new ResultWidget(Minecraft.getInstance().font, item, hasFeature));
    }

    private void check(final BiConsumer<Minecraft, ResultWidget> entryConsumer) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        final ResultWidget entry = this.resultListWidget.getSelected();
        if (entry == null) {
            return;
        }
        entryConsumer.accept(minecraft, entry);
    }

    private void selectEntry(final ResultWidget entry) {
        this.resultListWidget.setSelected(entry);
        if (!entry.getItem().equals(this.lastClickItemEntry)) {
            this.lastClickItemEntry = null;
        }
    }

    private boolean entryClickHandler(final double mouseY, final int button) {
        // entry select
        final int entryY = this.resultListWidget.getEntryY(mouseY);
        if (entryY >= 0) {
            final int entryIndex = entryY / this.resultListWidget.getEntryHeight();
            final ResultWidget entry = this.resultListWidget.at(entryIndex);
            if (entry != null) {
                this.selectEntry(entry);
            }
        }

        // double click exec
        final ResultWidget selectedEntry = this.resultListWidget.getSelected();
        if (selectedEntry != null) {
            final long timeMs = Util.getMillis();
            if (timeMs - this.lastClickTime <= 420
                && this.lastClickItemEntry != null
                && this.lastClickItemEntry.equals(selectedEntry.getItem())) {
                if (button == 0) {
                    this.giveItem();
                }
                return true;
            }

            this.lastClickTime = timeMs;
            this.lastClickItemEntry = selectedEntry.getItem();
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.isShiftDown = (modifiers & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.resultListWidget.isMouseOver(mouseX, mouseY)
            && this.entryClickHandler(mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // TODO: Configurable, also refactor this.
    public void giveItem() {
        this.check((client, entry) -> {
            inventoryHandler.giveItem(client, entry.getItem());
            this.onClose();
        });
    }

    public void updateResults() {
        this.search(this.searchboxWidget.getValue());
    }

    public ResultListWidget getResultList() {
        return resultListWidget;
    }

    public SearchboxWidget getSearchboxWidget() {
        return searchboxWidget;
    }

    public Queue<Item> getItemHistory() {
        return itemHistory.getItemHistory();
    }

    @Override
    public void onClose() {
        if (ConfigAgent.keepScreenOn == ConfigAgent.KeepScreen.SHIFT) {
            if (this.isShiftDown) return;
        }
        super.onClose();
    }
}

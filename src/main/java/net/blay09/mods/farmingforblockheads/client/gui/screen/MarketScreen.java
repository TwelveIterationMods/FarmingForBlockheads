package net.blay09.mods.farmingforblockheads.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.client.gui.widget.MarketFilterButton;
import net.blay09.mods.farmingforblockheads.container.MarketBuySlot;
import net.blay09.mods.farmingforblockheads.container.MarketClientContainer;
import net.blay09.mods.farmingforblockheads.container.MarketContainer;
import net.blay09.mods.farmingforblockheads.container.MarketFakeSlot;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

// TODO @MouseTweaksIgnore
public class MarketScreen extends ContainerScreen<MarketContainer> {

    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_Y = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_HEIGHT = 77;
    private static final int VISIBLE_ROWS = 4;

    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

    private final List<MarketFilterButton> filterButtons = Lists.newArrayList();
    private final MarketClientContainer clientContainer;

    private boolean isEventHandler;
    private int scrollBarScaledHeight;
    private int scrollBarXPos;
    private int scrollBarYPos;
    private int currentOffset;

    private int mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    private TextFieldWidget searchBar;

    public MarketScreen(MarketContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.clientContainer = (MarketClientContainer) container;
    }

    @Override
    public void init() {
        ySize = 174;
        super.init();

        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

        searchBar = new TextFieldWidget(fontRenderer, guiLeft + xSize - 78, guiTop - 5, 70, 10, searchBar, "");
        setFocusedDefault(searchBar);
        addButton(searchBar);

        updateCategoryFilters();

        if (!isEventHandler) {
            MinecraftForge.EVENT_BUS.register(this);
            isEventHandler = true;
        }

        recalculateScrollBar();
    }

    private void updateCategoryFilters() {
        for( MarketFilterButton filterButton : filterButtons ) {
            buttons.remove( filterButton );
            children.remove( filterButton );
        }
        filterButtons.clear();

        int curY = -80;
        IMarketCategory[] categories = clientContainer.getCategories().stream().sorted().toArray( IMarketCategory[]::new );
        for (IMarketCategory category : categories) {
            MarketFilterButton filterButton = new MarketFilterButton( width / 2 + 87, height / 2 + curY, clientContainer, category, button -> {
                if (clientContainer.getCurrentCategory() == category) {
                    clientContainer.setFilterCategory(null);
                } else {
                    clientContainer.setFilterCategory(category);
                }
                clientContainer.populateMarketSlots();
                setCurrentOffset(currentOffset);
            });

            addButton(filterButton);
            filterButtons.add(filterButton);

            curY += 20;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (Math.abs(delta) > 0f) {
            setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != -1 && mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && mouseX >= searchBar.x && mouseX < searchBar.x + searchBar.getWidth() && mouseY >= searchBar.y && mouseY < searchBar.y + searchBar.getHeight()) {
            searchBar.setText("");
            clientContainer.search(null);
            clientContainer.populateMarketSlots();
            setCurrentOffset(currentOffset);
            return true;
        } else if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
            mouseClickY = (int) mouseY;
            indexWhenClicked = currentOffset;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        boolean result = super.charTyped(c, keyCode);

        clientContainer.search(searchBar.getText());
        clientContainer.populateMarketSlots();
        setCurrentOffset(currentOffset);

        return result;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBar.keyPressed(keyCode, scanCode, modifiers) || searchBar.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                getMinecraft().player.closeScreen();
            } else {
                clientContainer.search(searchBar.getText());
                clientContainer.populateMarketSlots();
                setCurrentOffset(currentOffset);
            }

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        for (Button sortButton : filterButtons) {
            if (sortButton.isMouseOver(mouseX, mouseY) && sortButton.active) {
                renderTooltip(((MarketFilterButton) sortButton).getTooltipLines(), mouseX, mouseY);
            }
        }

        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (clientContainer.isDirty()) {
            updateCategoryFilters();
            recalculateScrollBar();
            clientContainer.setDirty(false);
        }

        FontRenderer fontRenderer = getMinecraft().fontRenderer;

        RenderSystem.color4f(1f, 1f, 1f, 1f);
        getMinecraft().getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop - 10, 0, 0, xSize, ySize + 10);
        if (container.getSelectedEntry() != null && !container.isReadyToBuy()) {
            blit(guiLeft + 43, guiTop + 40, 176, 0, 14, 14);
        }

        if (mouseClickY != -1) {
            float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(clientContainer.getFilteredListCount() / 3f) - VISIBLE_ROWS);
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        fontRenderer.drawStringWithShadow(I18n.format("container.farmingforblockheads:market"), guiLeft + 10, guiTop + 10, 0xFFFFFF);

        if (container.getSelectedEntry() == null) {
            drawCenteredString(fontRenderer, I18n.format("gui.farmingforblockheads:market.no_selection"), guiLeft + 49, guiTop + 65, 0xFFFFFF);
        } else {
            drawCenteredString(fontRenderer, getPriceText(container.getSelectedEntry()).getFormattedText(), guiLeft + 49, guiTop + 65, 0xFFFFFF);
        }

        fill(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }

    public Collection<MarketFilterButton> getFilterButtons() {
        return filterButtons;
    }

    @Override
    public void removed() {
        if (isEventHandler) {
            MinecraftForge.EVENT_BUS.unregister(this);
            isEventHandler = false;
        }
    }

    private void recalculateScrollBar() {
        int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(clientContainer.getFilteredListCount() / 3f)))));
        this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
        this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((clientContainer.getFilteredListCount() / 3f)) - VISIBLE_ROWS));
    }

    private void setCurrentOffset(int currentOffset) {
        this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(clientContainer.getFilteredListCount() / 3f) - VISIBLE_ROWS));

        clientContainer.setScrollOffset(this.currentOffset);

        recalculateScrollBar();
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        Slot hoverSlot = getSlotUnderMouse();
        if (hoverSlot != null && event.getItemStack() == hoverSlot.getStack()) {
            IMarketEntry hoverEntry = null;

            if (hoverSlot instanceof MarketFakeSlot) {
                hoverEntry = ((MarketFakeSlot) hoverSlot).getEntry();
            } else if (hoverSlot instanceof MarketBuySlot) {
                hoverEntry = container.getSelectedEntry();
            }

            if (hoverEntry != null) {
                event.getToolTip().add(getPriceTooltipText(hoverEntry));
            }
        }
    }

    private ITextComponent getPriceTooltipText(IMarketEntry entry) {
        ITextComponent result = new TranslationTextComponent("gui.farmingforblockheads:market.tooltip_cost", getPriceText(entry));
        result.getStyle().setColor(getPriceColor(entry));
        return result;
    }

    private ITextComponent getPriceText(IMarketEntry entry) {
        ITextComponent textComponent = new TranslationTextComponent("gui.farmingforblockheads:market.cost", entry.getCostItem().getCount(), entry.getCostItem().getDisplayName());
        textComponent.getStyle().setColor(getPriceColor(entry));
        return textComponent;
    }

    private TextFormatting getPriceColor(IMarketEntry entry) {
        TextFormatting color = TextFormatting.GREEN;
        String unlocalizedName = entry.getCostItem().getTranslationKey().toLowerCase(Locale.ENGLISH);
        if (unlocalizedName.contains("diamond")) {
            color = TextFormatting.AQUA;
        } else if (unlocalizedName.contains("gold")) {
            color = TextFormatting.YELLOW;
        } else if (unlocalizedName.contains("iron")) {
            color = TextFormatting.WHITE;
        }
        return color;
    }

}

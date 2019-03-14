package net.blay09.mods.farmingforblockheads.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.api.IMarketEntry;
import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.blay09.mods.farmingforblockheads.container.FakeSlotMarket;
import net.blay09.mods.farmingforblockheads.container.SlotMarketBuy;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yalter.mousetweaks.api.MouseTweaksIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@MouseTweaksIgnore
public class GuiMarket extends GuiContainer {

    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_Y = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_HEIGHT = 77;
    private static final int VISIBLE_ROWS = 4;

    private static final ResourceLocation TEXTURE = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

    private final ContainerMarketClient container;
    private final List<GuiButtonMarketFilter> filterButtons = Lists.newArrayList();

    private boolean isEventHandler;
    private int scrollBarScaledHeight;
    private int scrollBarXPos;
    private int scrollBarYPos;
    private int currentOffset;

    private int mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    private GuiTextField searchBar;

    public GuiMarket(ContainerMarketClient container) {
        super(container);
        this.container = container;
    }

    @Override
    public void initGui() {
        ySize = 174;
        super.initGui();

        searchBar = new GuiTextField(0, fontRenderer, guiLeft + xSize - 78, guiTop - 5, 70, 10);

        int id = 1;
        int curY = -80;
        IMarketCategory[] categories = MarketRegistry.getCategories().stream().sorted().toArray(IMarketCategory[]::new);
        for (IMarketCategory category : categories) {
            if (MarketRegistry.getGroupedEntries().get(category).isEmpty()) {
                continue;
            }

            GuiButtonMarketFilter filterButton = new GuiButtonMarketFilter(id++, width / 2 + 87, height / 2 + curY, container, category) {
                @Override
                public void onClick(double mouseX, double mouseY) {
                    if (container.getCurrentCategory() == category) {
                        container.setFilterCategory(null);
                    } else {
                        container.setFilterCategory(category);
                    }
                    container.populateMarketSlots();
                    setCurrentOffset(currentOffset);
                }
            };

            addButton(filterButton);
            filterButtons.add(filterButton);

            curY += 20;
        }

        if (!isEventHandler) {
            MinecraftForge.EVENT_BUS.register(this);
            isEventHandler = true;
        }

        recalculateScrollBar();
    }

    @Override
    public boolean mouseScrolled(double delta) {
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
        if (button == 1 && mouseX >= searchBar.x && mouseX < searchBar.x + searchBar.width && mouseY >= searchBar.y && mouseY < searchBar.y + searchBar.height) {
            searchBar.setText("");
            container.search(null);
            container.populateMarketSlots();
            setCurrentOffset(currentOffset);
            return true;
        } else if (searchBar.mouseClicked(mouseX, mouseY, button)) {
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
        if (searchBar.charTyped(c, keyCode)) {
            container.search(searchBar.getText());
            container.populateMarketSlots();
            setCurrentOffset(currentOffset);
            return true;
        }

        return super.charTyped(c, keyCode);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);

        for (GuiButton sortButton : filterButtons) {
            if (sortButton.isMouseOver() && sortButton.enabled) {
                drawHoveringText(((GuiButtonMarketFilter) sortButton).getTooltipLines(), mouseX, mouseY);
            }
        }

        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (container.isDirty()) {
            recalculateScrollBar();
            container.setDirty(false);
        }

        GlStateManager.color4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop - 10, 0, 0, xSize, ySize + 10);
        if (container.getSelectedEntry() != null && !container.isReadyToBuy()) {
            drawTexturedModalRect(guiLeft + 43, guiTop + 40, 176, 0, 14, 14);
        }

        if (mouseClickY != -1) {
            float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(container.getFilteredListCount() / 3f) - VISIBLE_ROWS);
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
            drawCenteredString(fontRenderer, getFormattedCostStringShort(container.getSelectedEntry()), guiLeft + 49, guiTop + 65, 0xFFFFFF);
        }

        GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        GlStateManager.color4f(1f, 1f, 1f, 1f);

        searchBar.drawTextField(mouseX, mouseY, partialTicks);
    }

    public Collection<GuiButtonMarketFilter> getFilterButtons() {
        return filterButtons;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (isEventHandler) {
            MinecraftForge.EVENT_BUS.unregister(this);
            isEventHandler = false;
        }
    }

    public void recalculateScrollBar() {
        int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(container.getFilteredListCount() / 3f)))));
        this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
        this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((container.getFilteredListCount() / 3f)) - VISIBLE_ROWS));
    }

    public void setCurrentOffset(int currentOffset) {
        this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getFilteredListCount() / 3f) - VISIBLE_ROWS));

        container.setScrollOffset(this.currentOffset);

        recalculateScrollBar();
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        Slot hoverSlot = getSlotUnderMouse();
        if (hoverSlot != null && event.getItemStack() == hoverSlot.getStack()) {
            IMarketEntry hoverEntry = null;

            if (hoverSlot instanceof FakeSlotMarket) {
                hoverEntry = ((FakeSlotMarket) hoverSlot).getEntry();
            } else if (hoverSlot instanceof SlotMarketBuy) {
                hoverEntry = container.getSelectedEntry();
            }

            if (hoverEntry != null) {
                event.getToolTip().add(getFormattedCostString(hoverEntry));
            }
        }
    }

    private String getFormattedCostString(IMarketEntry entry) {
        String color = TextFormatting.GREEN.toString();
        if (entry.getCostItem().getItem() == Items.DIAMOND) {
            color = TextFormatting.AQUA.toString();
        }
        return color + I18n.format("gui.farmingforblockheads:market.tooltip_cost", I18n.format("gui.farmingforblockheads:market.cost", entry.getCostItem().getCount(), entry.getCostItem().getDisplayName()));
    }

    private String getFormattedCostStringShort(IMarketEntry entry) {
        String color = TextFormatting.GREEN.toString();
        String unlocalizedName = entry.getCostItem().getTranslationKey().toLowerCase(Locale.ENGLISH);
        if (unlocalizedName.contains("diamond")) {
            color = TextFormatting.AQUA.toString();
        } else if (unlocalizedName.contains("gold")) {
            color = TextFormatting.YELLOW.toString();
        } else if (unlocalizedName.contains("iron")) {
            color = TextFormatting.WHITE.toString();
        }

        return color + I18n.format("gui.farmingforblockheads:market.cost", entry.getCostItem().getCount(), entry.getCostItem().getDisplayName());
    }

}

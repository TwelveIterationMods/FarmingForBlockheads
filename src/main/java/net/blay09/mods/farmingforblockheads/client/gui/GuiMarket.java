package net.blay09.mods.farmingforblockheads.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.blay09.mods.farmingforblockheads.container.FakeSlotMarket;
import net.blay09.mods.farmingforblockheads.container.SlotMarketBuy;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import yalter.mousetweaks.api.MouseTweaksIgnore;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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

		searchBar = new GuiTextField(0, fontRendererObj, guiLeft + xSize - 78, guiTop - 5, 70, 10);

		int id = 1;
		int curY = -80;
		for (MarketEntry.EntryType type : MarketEntry.EntryType.values()) {
			GuiButtonMarketFilter filterButton = new GuiButtonMarketFilter(id++, width / 2 + 87, height / 2 + curY, container, type);
			buttonList.add(filterButton);
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
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button instanceof GuiButtonMarketFilter) {
			if(container.getCurrentFilter() == ((GuiButtonMarketFilter) button).getFilterType()) {
				container.setFilterType(null);
			} else {
				container.setFilterType(((GuiButtonMarketFilter) button).getFilterType());
			}
			container.populateMarketSlots();
			recalculateScrollBar();
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta == 0) {
			return;
		}
		setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (state != -1 && mouseClickY != -1) {
			mouseClickY = -1;
			indexWhenClicked = 0;
			lastNumberOfMoves = 0;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		if (button == 1 && mouseX >= searchBar.xPosition && mouseX < searchBar.xPosition + searchBar.width && mouseY >= searchBar.yPosition && mouseY < searchBar.yPosition + searchBar.height) {
			searchBar.setText("");
			container.search(null);
			container.populateMarketSlots();
			recalculateScrollBar();
		} else {
			searchBar.mouseClicked(mouseX, mouseY, button);
		}
		if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
			mouseClickY = mouseY;
			indexWhenClicked = currentOffset;
		}
	}

	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		if (searchBar.textboxKeyTyped(c, keyCode)) {
			container.search(searchBar.getText());
			container.populateMarketSlots();
			recalculateScrollBar();
		} else {
			super.keyTyped(c, keyCode);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		for (GuiButton sortButton : filterButtons) {
			if (sortButton.isMouseOver() && sortButton.enabled) {
				drawHoveringText(((GuiButtonMarketFilter) sortButton).getTooltipLines(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		if (container.isDirty()) {
			recalculateScrollBar();
			container.setDirty(false);
		}

		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop - 10, 0, 0, xSize, ySize + 10);
		if(container.getSelectedEntry() != null && !container.isReadyToBuy()) {
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

		fontRendererObj.drawString(I18n.format("container.farmingforblockheads:market"), guiLeft + 10, guiTop + 10, 0xFFFFFF, true);

		if(container.getSelectedEntry() == null) {
			drawCenteredString(fontRendererObj, I18n.format("gui.farmingforblockheads:market.no_selection"), guiLeft + 49, guiTop + 65, 0xFFFFFF);
		} else {
			drawCenteredString(fontRendererObj, getFormattedCostStringShort(container.getSelectedEntry()), guiLeft + 49, guiTop + 65, 0xFFFFFF);
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

		GlStateManager.color(1f, 1f, 1f, 1f);

		searchBar.drawTextBox();
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
		//noinspection ConstantConditions
		if (hoverSlot != null && event.getItemStack() == hoverSlot.getStack()) {
			MarketEntry hoverEntry = null;

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

	private String getFormattedCostString(MarketEntry entry) {
		String color = TextFormatting.GREEN.toString();
		if(entry.getCostItem().getItem() == Items.DIAMOND) {
			color = TextFormatting.AQUA.toString();
		}
		return color + I18n.format("gui.farmingforblockheads:market.tooltip_cost", I18n.format("gui.farmingforblockheads:market.cost", entry.getCostItem().stackSize, entry.getCostItem().getDisplayName()));
	}

	private String getFormattedCostStringShort(MarketEntry entry) {
		String color = TextFormatting.GREEN.toString();
		if(entry.getCostItem().getItem() == Items.DIAMOND) {
			color = TextFormatting.AQUA.toString();
		}
		return color + I18n.format("gui.farmingforblockheads:market.cost", entry.getCostItem().stackSize, entry.getCostItem().getDisplayName());
	}

}

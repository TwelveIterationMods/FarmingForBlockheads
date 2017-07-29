package net.blay09.mods.farmingforblockheads.client.gui;

import java.util.List;

import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.blay09.mods.farmingforblockheads.registry.MarketEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import com.google.common.collect.Lists;

public class GuiButtonMarketFilter extends GuiButton {

	private final ContainerMarketClient container;
	private final MarketEntry.EntryType filterType;
	private final List<String> tooltipLines = Lists.newArrayList();

	public GuiButtonMarketFilter(int buttonId, int x, int y, ContainerMarketClient container, MarketEntry.EntryType filterType) {
		super(buttonId, x, y, 20, 20, "");
		this.container = container;
		this.filterType = filterType;
		this.tooltipLines.add(I18n.format(this.filterType.getTooltip()));
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		int texY = filterType.getIconTextureY();
		if(container.getCurrentFilter() != null && container.getCurrentFilter() != filterType) {
			texY += 40;
		} else if(hovered) {
			texY += 20;
		}
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(filterType.getIconTexture());
		drawTexturedModalRect(x, y, filterType.getIconTextureX(), texY, width, height);
	}

	public List<String> getTooltipLines() {
		return tooltipLines;
	}

	public MarketEntry.EntryType getFilterType() {
		return filterType;
	}

}
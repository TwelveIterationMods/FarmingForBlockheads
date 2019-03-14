package net.blay09.mods.farmingforblockheads.client.gui;

import com.google.common.collect.Lists;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class GuiButtonMarketFilter extends GuiButton {

    private final ContainerMarketClient container;
    private final IMarketCategory category;
    private final List<String> tooltipLines = Lists.newArrayList();

    public GuiButtonMarketFilter(int buttonId, int x, int y, ContainerMarketClient container, IMarketCategory category) {
        super(buttonId, x, y, 20, 20, "");
        this.container = container;
        this.category = category;
        this.tooltipLines.add(I18n.format(this.category.getTooltipLangKey()));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = category.getIconTextureY();
        if (container.getCurrentCategory() != null && container.getCurrentCategory() != category) {
            texY += 40;
        } else if (hovered) {
            texY += 20;
        }
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(category.getIconTexture());
        drawTexturedModalRect(x, y, category.getIconTextureX(), texY, width, height);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }

    public IMarketCategory getCategory() {
        return category;
    }

}

package net.blay09.mods.farmingforblockheads.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.container.MarketClientContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class MarketFilterButton extends Button {

    private final MarketClientContainer container;
    private final IMarketCategory category;
    private final List<String> tooltipLines = Lists.newArrayList();

    public MarketFilterButton(int x, int y, MarketClientContainer container, IMarketCategory category, IPressable pressable) {
        super(x, y, 20, 20, "", pressable);
        this.container = container;
        this.category = category;
        this.tooltipLines.add(I18n.format(this.category.getTooltipLangKey()));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = category.getIconTextureY();
        if (container.getCurrentCategory() != null && container.getCurrentCategory() != category) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(category.getIconTexture());
        blit(x, y, category.getIconTextureX(), texY, width, height);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }

    public IMarketCategory getCategory() {
        return category;
    }

}

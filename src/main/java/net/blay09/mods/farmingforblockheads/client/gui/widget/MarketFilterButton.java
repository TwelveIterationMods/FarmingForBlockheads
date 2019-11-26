package net.blay09.mods.farmingforblockheads.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.container.MarketClientContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class MarketFilterButton extends Button {

    private static final ResourceLocation ICONS = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

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

        int texY = 14;
        if (container.getCurrentCategory() != null && container.getCurrentCategory() != category) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(ICONS);
        blit(x, y, 176, texY, width, height);

        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(category.getIconStack(), x + 2, y + 2);
    }

    public List<String> getTooltipLines() {
        return tooltipLines;
    }

    public IMarketCategory getCategory() {
        return category;
    }

}

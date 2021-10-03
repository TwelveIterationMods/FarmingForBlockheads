package net.blay09.mods.farmingforblockheads.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.farmingforblockheads.FarmingForBlockheads;
import net.blay09.mods.farmingforblockheads.api.IMarketCategory;
import net.blay09.mods.farmingforblockheads.menu.MarketClientMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MarketFilterButton extends Button {

    private static final ResourceLocation ICONS = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");

    private final MarketClientMenu container;
    private final IMarketCategory category;
    private final List<Component> tooltipLines = Lists.newArrayList();

    public MarketFilterButton(int x, int y, MarketClientMenu container, IMarketCategory category, OnPress pressable) {
        super(x, y, 20, 20, new TextComponent(""), pressable);
        this.container = container;
        this.category = category;
        this.tooltipLines.add(new TranslatableComponent(this.category.getTooltipLangKey()));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int texY = 14;
        if (container.getCurrentCategory() != null && container.getCurrentCategory() != category) {
            texY += 40;
        } else if (isHovered) {
            texY += 20;
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, ICONS);
        blit(poseStack, x, y, 176, texY, width, height);

        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(category.getIconStack(), x + 2, y + 2);
    }

    public List<Component> getTooltipLines() {
        return tooltipLines;
    }

    public IMarketCategory getCategory() {
        return category;
    }

}

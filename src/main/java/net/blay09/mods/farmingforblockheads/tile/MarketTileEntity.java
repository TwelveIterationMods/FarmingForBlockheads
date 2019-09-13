package net.blay09.mods.farmingforblockheads.tile;

import net.blay09.mods.farmingforblockheads.container.MarketContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MarketTileEntity extends TileEntity implements INamedContainerProvider {
    public MarketTileEntity() {
        super(ModTileEntities.market);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.farmingforblockheads.market");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MarketContainer(windowId, playerInventory, pos);
    }
}

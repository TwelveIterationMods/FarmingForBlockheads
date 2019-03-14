package net.blay09.mods.farmingforblockheads.tile;

import net.blay09.mods.farmingforblockheads.container.ContainerMarket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

import javax.annotation.Nullable;

public class TileMarket extends TileEntity implements IInteractionObject {
    public TileMarket() {
        super(ModTiles.market);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerMarket(entityPlayer, pos);
    }

    @Override
    public String getGuiID() {
        return "farmingforblockheads:market";
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("block.farmingforblockheads.market");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }
}

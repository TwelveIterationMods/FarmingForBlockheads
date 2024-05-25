package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MarketBlockEntity extends BalmBlockEntity implements BalmMenuProvider<MarketMenu.Data> {
    public MarketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.market.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.farmingforblockheads.market");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return new MarketMenu(windowId, playerInventory, worldPosition, getPresetFilter(), getCategoryFilter());
    }

    @NotNull
    private static Set<ResourceLocation> getCategoryFilter() {
        return Set.of();
    }

    @NotNull
    private static Set<ResourceLocation> getPresetFilter() {
        return Set.of();
    }

    @Override
    public MarketMenu.Data getScreenOpeningData(ServerPlayer serverPlayer) {
        return new MarketMenu.Data(worldPosition, getPresetFilter(), getCategoryFilter());
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MarketMenu.Data> getScreenStreamCodec() {
        return MarketMenu.STREAM_CODEC;
    }
}

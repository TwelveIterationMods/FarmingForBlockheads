package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MarketBlockEntity extends BalmBlockEntity implements BalmMenuProvider {
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
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
        final var presetFilter = getPresetFilter();
        buf.writeInt(presetFilter.size());
        for (ResourceLocation location : presetFilter) {
            buf.writeResourceLocation(location);
        }
        final var categoryFilter = getCategoryFilter();
        buf.writeInt(categoryFilter.size());
        for (ResourceLocation location : categoryFilter) {
            buf.writeResourceLocation(location);
        }
    }
}

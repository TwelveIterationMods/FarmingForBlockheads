package net.blay09.mods.farmingforblockheads.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.menu.MarketMenu;
import net.blay09.mods.farmingforblockheads.mixin.RecipeManagerAccessor;
import net.blay09.mods.farmingforblockheads.network.MarketCategoriesMessage;
import net.blay09.mods.farmingforblockheads.network.MarketRecipesMessage;
import net.blay09.mods.farmingforblockheads.recipe.MarketRecipe;
import net.blay09.mods.farmingforblockheads.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MarketBlockEntity extends BalmBlockEntity implements BalmMenuProvider<BlockPos> {
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
        return new MarketMenu(windowId, playerInventory, worldPosition);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
        return worldPosition;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    public void openMenu(Player player) {
        Balm.getNetworking().openGui(player, this);
        final var categories = FarmingForBlockheadsAPI.getMarketCategories();
        Balm.getNetworking().sendTo(player, new MarketCategoriesMessage(categories));
        final var recipeManager = level.getServer().getRecipeManager();
        if (recipeManager instanceof RecipeManagerAccessor accessor) {
            final var displays = new ArrayList<RecipeDisplayEntry>();
            final var recipes = accessor.getRecipes().byType(ModRecipes.marketRecipeType);
            for (final var recipeHolder : recipes) {
                recipeManager.listDisplaysForRecipe(recipeHolder.id(), displays::add);
            }
            Balm.getNetworking().sendTo(player, new MarketRecipesMessage(displays));
        }
    }
}

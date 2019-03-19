package net.blay09.mods.farmingforblockheads.tile;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.blay09.mods.farmingforblockheads.block.BlockChickenNest;
import net.blay09.mods.farmingforblockheads.block.BlockFeedingTrough;
import net.blay09.mods.farmingforblockheads.block.BlockMarket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModTiles {

    public static TileEntityType<TileChickenNest> chickenNest;
    public static TileEntityType<TileFeedingTrough> feedingTrough;
    public static TileEntityType<TileMarket> market;

    public static void register(IForgeRegistry<TileEntityType<?>> registry) {
        registry.registerAll(
                chickenNest = build(TileChickenNest::new, BlockChickenNest.registryName),
                feedingTrough = build(TileFeedingTrough::new, BlockFeedingTrough.registryName),
                market = build(TileMarket::new, BlockMarket.registryName)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, ResourceLocation registryName) {
        //noinspection ConstantConditions
        return (TileEntityType<T>) TileEntityType.Builder.create(factory).build(dataFixerType(registryName)).setRegistryName(registryName);
    }

    @Nullable
    private static Type<?> dataFixerType(ResourceLocation registryName) {
        try {
            return DataFixesManager.getDataFixer()
                    .getSchema(DataFixUtils.makeKey(1519))
                    .getChoiceType(TypeReferences.BLOCK_ENTITY, registryName.toString());
        } catch (IllegalArgumentException e) {
            if (SharedConstants.developmentMode) {
                throw e;
            }
        }
        return null;
    }
}

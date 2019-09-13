package net.blay09.mods.farmingforblockheads.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ModContainers {
    public static ContainerType<MarketContainer> market;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(market = register("market", (windowId, playerInventory, data) -> {
            BlockPos pos = data.readBlockPos();
            return new MarketClientContainer(windowId, playerInventory, pos);
        }));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}

package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.api.MarketRegistryReloadEvent;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.ClientProxy;
import net.blay09.mods.farmingforblockheads.client.ModRenderers;
import net.blay09.mods.farmingforblockheads.client.ModScreens;
import net.blay09.mods.farmingforblockheads.compat.Compat;
import net.blay09.mods.farmingforblockheads.container.ModContainers;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.blay09.mods.farmingforblockheads.loot.ModLootModifiers;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.market.MarketRegistryLoader;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.blay09.mods.farmingforblockheads.tile.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FarmingForBlockheads.MOD_ID)
public class FarmingForBlockheads {

    public static final String MOD_ID = "farmingforblockheads";

    public static Logger logger = LogManager.getLogger();

    public static final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.market);
        }
    };

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public FarmingForBlockheads() {
        FarmingForBlockheadsAPI.__setupAPI(new InternalMethodsImpl());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processInterMod);
        MinecraftForge.EVENT_BUS.addListener(this::setupServer);
        MinecraftForge.EVENT_BUS.addListener(this::setupMarketRegistry);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, this::registerEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(GlobalLootModifierSerializer.class, this::registerLootModifiers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, this::registerSounds);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FarmingForBlockheadsConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FarmingForBlockheadsConfig.clientSpec);

        DeferredWorkQueue.runLater(NetworkHandler::init);
    }

    private void setupClient(FMLClientSetupEvent event) {
        ModScreens.register();
        ModRenderers.register();
    }

    private void setupServer(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager resourceManager = event.getServer().getResourceManager();
        resourceManager.addReloadListener(new MarketRegistryLoader());
    }

    private void setupMarketRegistry(MarketRegistryReloadEvent.Pre event) {
        FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_SEEDS, "gui.farmingforblockheads:market.tooltip_seeds", new ItemStack(Items.WHEAT_SEEDS), 10);
        FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_SAPLINGS, "gui.farmingforblockheads:market.tooltip_saplings", new ItemStack(Items.OAK_SAPLING), 20);
        FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_FLOWERS, "gui.farmingforblockheads:market.tooltip_flowers", new ItemStack(Items.DANDELION), 30);
        FarmingForBlockheadsAPI.registerMarketCategory(FarmingForBlockheadsAPI.MARKET_CATEGORY_OTHER, "gui.farmingforblockheads:market.tooltip_other", new ItemStack(Items.BONE_MEAL), 40);

        buildSoftDependProxy(Compat.FORESTRY, "net.blay09.mods.farmingforblockheads.compat.ForestryAddon");
        buildSoftDependProxy(Compat.NATURA, "net.blay09.mods.farmingforblockheads.compat.NaturaAddon");
        buildSoftDependProxy(Compat.TERRAQUEOUS, "net.blay09.mods.farmingforblockheads.compat.TerraqueousAddon");
    }

    private void buildSoftDependProxy(String modId, String className) {
        if (ModList.get().isLoaded(modId)) {
            try {
                Class.forName(className).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("Failed to load Farming for Blockheads compat for mod id {}: ", modId, e);
            }
        }
    }

    private void processInterMod(InterModProcessEvent event) {
        IMCHandler.handleIMCMessage(event);
    }

    private void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    private void registerItems(RegistryEvent.Register<Item> event) {
        ModBlocks.registerItemBlocks(event.getRegistry());
        ModItems.register(event.getRegistry());
    }

    private void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        ModSounds.register(event.getRegistry());
    }

    private void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ModTileEntities.register(event.getRegistry());
    }

    private void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ModContainers.register(event.getRegistry());
    }

    private void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.register(event.getRegistry());
    }

    private void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        ModLootModifiers.register(event.getRegistry());
    }

}

package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.ModScreens;
import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.FeedingTroughRenderer;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.compat.VanillaAddon;
import net.blay09.mods.farmingforblockheads.container.ModContainers;
import net.blay09.mods.farmingforblockheads.entity.MerchantEntity;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.AbstractRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.blay09.mods.farmingforblockheads.tile.ChickenNestTileEntity;
import net.blay09.mods.farmingforblockheads.tile.FeedingTroughTileEntity;
import net.blay09.mods.farmingforblockheads.tile.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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

    public static File configDir;

    public FarmingForBlockheads() {
        FarmingForBlockheadsAPI.__setupAPI(new InternalMethodsImpl());

        final ResourceLocation CATEGORY_ICONS = new ResourceLocation(FarmingForBlockheads.MOD_ID, "textures/gui/market.png");
        FarmingForBlockheadsAPI.registerMarketCategory(new ResourceLocation(MOD_ID, "seeds"), "gui.farmingforblockheads:market.tooltip_seeds", CATEGORY_ICONS, 196, 14, 10);
        FarmingForBlockheadsAPI.registerMarketCategory(new ResourceLocation(MOD_ID, "saplings"), "gui.farmingforblockheads:market.tooltip_saplings", CATEGORY_ICONS, 196 + 20, 14, 20);
        FarmingForBlockheadsAPI.registerMarketCategory(new ResourceLocation(MOD_ID, "flowers"), "gui.farmingforblockheads:market.tooltip_flowers", CATEGORY_ICONS, 176, 74, 30);
        FarmingForBlockheadsAPI.registerMarketCategory(new ResourceLocation(MOD_ID, "other"), "gui.farmingforblockheads:market.tooltip_other", CATEGORY_ICONS, 196 + 40, 14, 40);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processInterMod);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(EntityType.class, this::registerEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, this::registerSounds);

        DeferredWorkQueue.runLater(NetworkHandler::init);
    }

    private void setup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            new VanillaAddon();

            MarketRegistry.INSTANCE.load(configDir);
        });
    }

    private void setupClient(FMLClientSetupEvent event) {
        ModScreens.register();

        RenderingRegistry.registerEntityRenderingHandler(MerchantEntity.class, RenderMerchant::new);

        ClientRegistry.bindTileEntitySpecialRenderer(ChickenNestTileEntity.class, new ChickenNestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FeedingTroughTileEntity.class, new FeedingTroughRenderer());
    }

    private void serverStarting(FMLServerStartingEvent event) {
        CommandFarmingForBlockheads.register(event.getCommandDispatcher());
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

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (AbstractRegistry.registryErrors.size() > 0) {
            event.getPlayer().sendStatusMessage(new StringTextComponent(TextFormatting.RED + "There were errors loading the Farming for Blockheads registries:"), false);
            TextFormatting lastFormatting = TextFormatting.WHITE;
            for (String error : AbstractRegistry.registryErrors) {
                event.getPlayer().sendStatusMessage(new StringTextComponent(lastFormatting + "* " + error), false);
                lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
            }
        }
    }

}

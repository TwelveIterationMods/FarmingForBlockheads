package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.api.FarmingForBlockheadsAPI;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.client.gui.GuiMarket;
import net.blay09.mods.farmingforblockheads.client.render.ChickenNestRenderer;
import net.blay09.mods.farmingforblockheads.client.render.FeedingTroughRenderer;
import net.blay09.mods.farmingforblockheads.client.render.RenderMerchant;
import net.blay09.mods.farmingforblockheads.compat.VanillaAddon;
import net.blay09.mods.farmingforblockheads.container.ContainerMarketClient;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.blay09.mods.farmingforblockheads.entity.ModEntities;
import net.blay09.mods.farmingforblockheads.item.ModItems;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.AbstractRegistry;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.sound.ModSounds;
import net.blay09.mods.farmingforblockheads.tile.ModTiles;
import net.blay09.mods.farmingforblockheads.tile.TileChickenNest;
import net.blay09.mods.farmingforblockheads.tile.TileFeedingTrough;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> it -> {
            EntityPlayer player = Minecraft.getInstance().player;
            switch (it.getId().getPath()) {
                case "market":
                    BlockPos pos = it.getAdditionalData().readBlockPos();
                    return new GuiMarket(new ContainerMarketClient(player, pos));
            }

            return null;
        });
    }

    public void setup(FMLCommonSetupEvent event) {
        NetworkHandler.init();


        new VanillaAddon();
//        buildSoftDependProxy(Compat.HARVESTCRAFT, "net.blay09.mods.farmingforblockheads.compat.HarvestcraftAddon");
//        buildSoftDependProxy(Compat.FORESTRY, "net.blay09.mods.farmingforblockheads.compat.ForestryAddon");
//        buildSoftDependProxy(Compat.AGRICRAFT, "net.blay09.mods.farmingforblockheads.compat.AgriCraftAddon");
//        buildSoftDependProxy(Compat.BIOMESOPLENTY, "net.blay09.mods.farmingforblockheads.compat.BiomesOPlentyAddon");
//        buildSoftDependProxy(Compat.NATURA, "net.blay09.mods.farmingforblockheads.compat.NaturaAddon");
//        buildSoftDependProxy(Compat.TERRAQUEOUS, "net.blay09.mods.farmingforblockheads.compat.TerraqueousAddon");

        MarketRegistry.INSTANCE.load(configDir);
    }

    public void setupClient(FMLClientSetupEvent event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            RenderingRegistry.registerEntityRenderingHandler(EntityMerchant.class, RenderMerchant::new);

            ClientRegistry.bindTileEntitySpecialRenderer(TileChickenNest.class, new ChickenNestRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileFeedingTrough.class, new FeedingTroughRenderer());
        });
    }

    public void serverStarting(FMLServerStartingEvent event) {
        CommandFarmingForBlockheads.register(event.getCommandDispatcher());
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
        ModTiles.register(event.getRegistry());
    }

    private void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModEntities.register(event.getRegistry());
//        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID + ":merchant"), EntityMerchant.class, "merchant", 0, instance, 64, 3, true);
    }

    //    @SubscribeEvent TODO
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (AbstractRegistry.registryErrors.size() > 0) {
            event.getPlayer().sendStatusMessage(new TextComponentString(TextFormatting.RED + "There were errors loading the Farming for Blockheads registries:"), false);
            TextFormatting lastFormatting = TextFormatting.WHITE;
            for (String error : AbstractRegistry.registryErrors) {
                event.getPlayer().sendStatusMessage(new TextComponentString(lastFormatting + "* " + error), false);
                lastFormatting = lastFormatting == TextFormatting.GRAY ? TextFormatting.WHITE : TextFormatting.GRAY;
            }
        }
    }

}

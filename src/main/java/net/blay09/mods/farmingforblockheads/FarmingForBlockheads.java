package net.blay09.mods.farmingforblockheads;

import net.blay09.mods.farmingforblockheads.block.BlockMarket;
import net.blay09.mods.farmingforblockheads.block.ModBlocks;
import net.blay09.mods.farmingforblockheads.entity.EntityMerchant;
import net.blay09.mods.farmingforblockheads.network.GuiHandler;
import net.blay09.mods.farmingforblockheads.network.NetworkHandler;
import net.blay09.mods.farmingforblockheads.registry.MarketRegistry;
import net.blay09.mods.farmingforblockheads.tile.TileMarket;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = FarmingForBlockheads.MOD_ID, name = "Farming for Blockheads")
@Mod.EventBusSubscriber
public class FarmingForBlockheads {

	public static final String MOD_ID = "farmingforblockheads";

	@Mod.Instance(MOD_ID)
	public static FarmingForBlockheads instance;

	@SidedProxy(clientSide = "net.blay09.mods.farmingforblockheads.client.ClientProxy", serverSide = "net.blay09.mods.farmingforblockheads.CommonProxy")
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getLogger();

	public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public Item getTabIconItem() {
			//noinspection ConstantConditions
			return Item.getItemFromBlock(ModBlocks.market);
		}
	};

	public static File configDir;
	private Configuration config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configDir = new File(event.getModConfigurationDirectory(), "FarmingForBlockheads");
		if (!configDir.exists() && !configDir.mkdirs()) {
			throw new RuntimeException("Couldn't create Farming for Blockheads configuration directory");
		}
		config = new Configuration(new File(configDir, "FarmingForBlockheads.cfg"));
		config.load();
		ModConfig.preInit(config);

		GameRegistry.registerTileEntity(TileMarket.class, MOD_ID + ":market");

		proxy.preInit();

		if(config.hasChanged()) {
			config.save();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		ModRecipes.init();
		MarketRegistry.INSTANCE.load(configDir);

		EntityRegistry.registerModEntity(EntityMerchant.class, "merchant", 0, this, 64, 3, true);

		proxy.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll((ModBlocks.market = new BlockMarket()));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new ItemBlock(ModBlocks.market).setRegistryName(ModBlocks.market.getRegistryName()));
	}

}

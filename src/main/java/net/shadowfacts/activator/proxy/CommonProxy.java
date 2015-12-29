package net.shadowfacts.activator.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.compat.ModCompat;
import net.shadowfacts.activator.event.FMLEventHandler;
import net.shadowfacts.activator.gui.GuiHandler;
import net.shadowfacts.activator.network.PacketRequestTEUpdate;
import net.shadowfacts.activator.network.PacketUpdateTE;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new FMLEventHandler());

        ActivatorConfig.initialize(event);

        Activator.items.initializeItems();
        Activator.blocks.initializeBlocks();

        registerTileEntities();

        ModAchievements.registerAchievements();

		Activator.network = NetworkRegistry.INSTANCE.newSimpleChannel(Activator.name);
		registerPackets();

		NetworkRegistry.INSTANCE.registerGuiHandler(Activator.instance, new GuiHandler());

		ModCompat.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
		registerRecipes();
		registerOreDict();

		ModCompat.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		ModCompat.postInit(event);
	}

    private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityActivator.class, "activator");
		GameRegistry.registerTileEntity(TileEntityRedstoneActivator.class, "activator.redstone");
		GameRegistry.registerTileEntity(TileEntityRFActivator.class, "activator.rf");
    }

	private void registerPackets() {
		Activator.network.registerMessage(PacketUpdateTE.ServerHandler.class, PacketUpdateTE.class, 0, Side.SERVER);
		Activator.network.registerMessage(PacketUpdateTE.ClientHandler.class, PacketUpdateTE.class, 0, Side.CLIENT);
		Activator.network.registerMessage(PacketRequestTEUpdate.ServerHandler.class, PacketRequestTEUpdate.class, 1, Side.SERVER);
	}

	private void registerRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(Activator.items.gear), " S ", "SCS", " S ", 'S', Items.stick, 'C', Blocks.cobblestone);

		if (ActivatorConfig.basicEnabled) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Activator.blocks.activator), "IPI", "IGI", "IBI", 'I', "ingotIron", 'P', Blocks.piston, 'G', "itemBasicGear", 'B', Blocks.stone_button));
		}
		if (ActivatorConfig.redstoneEnabled) {
			GameRegistry.addShapelessRecipe(new ItemStack(Activator.blocks.redstoneActivator), Activator.blocks.activator, Items.redstone, Items.redstone);
			GameRegistry.addShapelessRecipe(new ItemStack(Activator.blocks.redstoneActivator, 3), Activator.blocks.activator, Activator.blocks.activator, Activator.blocks.activator, Items.redstone, Items.redstone, Items.redstone, Items.redstone, Items.redstone, Items.redstone);
		}
	}

	private void registerOreDict() {
		OreDictionary.registerOre("itemBasicGear", Activator.items.gear);
	}

	public World getClientWorld() {
		return null;
	}

}

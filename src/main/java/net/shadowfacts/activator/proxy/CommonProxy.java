package net.shadowfacts.activator.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.gui.GUIHandler;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;
import net.shadowfacts.shadowmc.proxy.BaseProxy;

/**
 * @author shadowfacts
 */
public class CommonProxy extends BaseProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		Activator.items.initializeItems();
		Activator.blocks.initializeBlocks();

		registerTileEntities();

		NetworkRegistry.INSTANCE.registerGuiHandler(Activator.instance, new GUIHandler());

		registerOreDict();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		registerRecipes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityActivator.class, "activator");
		GameRegistry.registerTileEntity(TileEntityRedstoneActivator.class, "activator.redstone");
		GameRegistry.registerTileEntity(TileEntityRFActivator.class, "activator.rf");
	}

	private void registerOreDict() {
		OreDictionary.registerOre("itemBasicGear", Activator.items.gear);
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

	public World getClientWorld() {
		return null;
	}

}

package net.shadowfacts.activator.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.activator.Activator;
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
		Activator.blocks.initializeBlocks();

		registerTileEntities();

		NetworkRegistry.INSTANCE.registerGuiHandler(Activator.instance, new GUIHandler());
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityActivator.class, "activator");
		GameRegistry.registerTileEntity(TileEntityRedstoneActivator.class, "activator.redstone");
		GameRegistry.registerTileEntity(TileEntityRFActivator.class, "activator.rf");
	}

	public World getClientWorld() {
		return null;
	}

}

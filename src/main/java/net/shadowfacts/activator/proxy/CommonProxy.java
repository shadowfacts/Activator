package net.shadowfacts.activator.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.achievement.ModAchievements;
import net.shadowfacts.activator.compat.ModCompat;
import net.shadowfacts.activator.event.FMLEventHandler;
import net.shadowfacts.activator.gui.GuiHandler;
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

		registerRecipes();

		Activator.network = NetworkRegistry.INSTANCE.newSimpleChannel(Activator.name);
		registerPackets();

		NetworkRegistry.INSTANCE.registerGuiHandler(Activator.instance, new GuiHandler());

		ModCompat.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
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
	}

	private void registerRecipes() {
//		TODO: Recipes
		if (ActivatorConfig.basicEnabled) ;
		if (ActivatorConfig.redstoneEnabled) ;
		if (ActivatorConfig.rfEnabled) ;
		if (ActivatorConfig.euEnabled) ;
	}

	public World getClientWorld() {
		return null;
	}

}

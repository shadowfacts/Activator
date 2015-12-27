package net.shadowfacts.activator.proxy;

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
import net.shadowfacts.activator.gui.GuiHandler;
import net.shadowfacts.activator.network.PacketUpdateTE;
import net.shadowfacts.activator.tileentity.TileEntityActivator;

/**
 * @author shadowfacts
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
        ActivatorConfig.initialize(event);

        Activator.items.initializeItems();
        Activator.blocks.initializeBlocks();

        registerTileEntities();

        ModAchievements.registerAchievements();

		Activator.network = NetworkRegistry.INSTANCE.newSimpleChannel(Activator.name);
		registerPackets();

		NetworkRegistry.INSTANCE.registerGuiHandler(Activator.instance, new GuiHandler());
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}

    private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityActivator.class, "activator");
    }

	private void registerPackets() {
		Activator.network.registerMessage(PacketUpdateTE.ServerHandler.class, PacketUpdateTE.class, 0, Side.SERVER);
		Activator.network.registerMessage(PacketUpdateTE.ClientHandler.class, PacketUpdateTE.class, 0, Side.CLIENT);
	}

	public World getClientWorld() {
		return null;
	}

}

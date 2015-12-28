package net.shadowfacts.activator;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.shadowfacts.activator.block.ModBlocks;
import net.shadowfacts.activator.item.ModItems;
import net.shadowfacts.activator.proxy.CommonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author shadowfacts
 */
@Mod(modid = Activator.modId, name = Activator.name, version = Activator.version, acceptableRemoteVersions = Activator.acceptableRemoteVersion)
public class Activator {

	public static final String modId = "activator";
	public static final String name = "Activator";
	public static final String version = "@VERSION@";
	public static final String acceptableRemoteVersion = "1.0.*";
	public static final String serverProxy = "net.shadowfacts.activator.proxy.CommonProxy";
	public static final String clientProxy = "net.shadowfacts.activator.proxy.ClientProxy";

    public static final Logger log = LogManager.getLogger(name);

	@SidedProxy(serverSide = serverProxy, clientSide = clientProxy)
	public static CommonProxy proxy;

	@Mod.Instance
	public static Activator instance;

	public static SimpleNetworkWrapper network;

//  Content
    public static ModBlocks blocks = new ModBlocks();
    public static ModItems items = new ModItems();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}

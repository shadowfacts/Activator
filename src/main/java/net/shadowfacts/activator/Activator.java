package net.shadowfacts.activator;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.activator.block.ModBlocks;
import net.shadowfacts.activator.compat.CompatActuallyAdditions;
import net.shadowfacts.activator.item.ModItems;
import net.shadowfacts.activator.proxy.CommonProxy;
import net.shadowfacts.shadowmc.util.LogHelper;

/**
 * @author shadowfacts
 */
@Mod(modid = Activator.modId, name = Activator.name, version = Activator.version)
public class Activator {

	public static final String modId = "Activator";
	public static final String name = modId;
	public static final String version = "1.1.0";
	public static final String depString = "required-after:shadowmc;after:ActuallyAdditions;";

	public static LogHelper log = new LogHelper(modId);

	@SidedProxy(serverSide = "net.shadowfacts.activator.proxy.CommonProxy", clientSide = "net.shadowfacts.activator.proxy.ClientProxy")
	public static CommonProxy proxy;

	@Mod.Instance(modId)
	public static Activator instance;

//	Content
	public static ModItems items = new ModItems();
	public static ModBlocks blocks = new ModBlocks();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ActivatorConfig.init(event.getModConfigurationDirectory());
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		if (Loader.isModLoaded("ActuallyAdditions")) {
			new CompatActuallyAdditions().init(event);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}

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
import net.shadowfacts.shadowmc.BaseMod;
import net.shadowfacts.shadowmc.proxy.BaseProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author shadowfacts
 */
@Mod(modid = Activator.modId, name = Activator.name, version = Activator.version, dependencies = Activator.depString)
public class Activator extends BaseMod {

	public static final String modId = "Activator";
	public static final String name = modId;
	public static final String version = "1.1.0";
	public static final String depString = "required-after:shadowmc;after:ActuallyAdditions;";

	public static Logger log = LogManager.getLogger(modId);

	@SidedProxy(serverSide = "net.shadowfacts.activator.proxy.CommonProxy", clientSide = "net.shadowfacts.activator.proxy.ClientProxy")
	public static CommonProxy proxy;

	@Mod.Instance(modId)
	public static Activator instance;

//	Content
	public static ModItems items = new ModItems();
	public static ModBlocks blocks = new ModBlocks();

	@Override
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
//		getCompatManager().registerModule(CompatActuallyAdditions.class);
	}

	@Override
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		super.init(event);
		if (Loader.isModLoaded("ActuallyAdditions")) {
			new CompatActuallyAdditions().init(event);	
		}
	}

	@Override
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public String getModId() {
		return modId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersionString() {
		return version;
	}

	@Override
	public Class<?> getConfigClass() {
		return ActivatorConfig.class;
	}

	@Override
	public BaseProxy getProxy() {
		return proxy;
	}
}

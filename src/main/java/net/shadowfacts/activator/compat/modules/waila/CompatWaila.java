package net.shadowfacts.activator.compat.modules.waila;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.shadowfacts.activator.block.BlockRFActivator;
import net.shadowfacts.activator.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("Waila")
public class CompatWaila {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", "net.shadowfacts.activator.compat.CompatWaila.callback");
	}

	public static void callback(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new RFActivatorDataProvider(), BlockRFActivator.class);
	}

}

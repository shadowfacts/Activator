package net.shadowfacts.activator.compat.modules;

import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.ItemStack;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("NotEnoughItems")
public class CompatNEI {

	@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event) {
		if (!ActivatorConfig.basicEnabled) API.hideItem(new ItemStack(Activator.blocks.activator));
		if (!ActivatorConfig.redstoneEnabled) API.hideItem(new ItemStack(Activator.blocks.redstoneActivator));
		if (!ActivatorConfig.rfEnabled) API.hideItem(new ItemStack(Activator.blocks.rfActivator));
	}

}

package net.shadowfacts.activator.compat.modules;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("EnderIO")
public class CompatEnderIO {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		if (ActivatorConfig.rfEnabled) {
			ItemStack electricalSteel = new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 0);
			ItemStack basicCapacitor = new ItemStack(GameRegistry.findItem("EnderIO", "itemBasicCapacitor"), 1, 0);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Activator.blocks.rfActivator), "APA", "AGA", "ACA", 'A', electricalSteel, 'P', Blocks.piston, 'G', "itemBasicGear", 'C', basicCapacitor));
		}
	}

}

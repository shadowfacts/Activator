package net.shadowfacts.activator.compat.modules;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("ThermalFoundation")
public class CompatThermalFoundation {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Activator.blocks.rfActivator), "TPT", "TGT", "TBT", 'T', "ingotTin", 'P', Blocks.piston, 'G', "gearIron", 'B', Blocks.stone_button));
	}

}

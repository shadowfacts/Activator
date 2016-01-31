package net.shadowfacts.activator.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.shadowmc.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("ActuallyAdditions")
public class CompatActuallyAdditions {

	@Compat.Init
	public void init(FMLInitializationEvent event) {
		if (ActivatorConfig.rfEnabled) {
			ItemStack redCrystal = new ItemStack(GameRegistry.findItem("ActuallyAdditions", "itemCrystal"));
			ItemStack coil = new ItemStack(GameRegistry.findItem("ActuallyAdditions", "itemMisc"), 1, 7);

			GameRegistry.addShapelessRecipe(new ItemStack(Activator.blocks.rfActivator), Activator.blocks.redstoneActivator, redCrystal, coil);
			GameRegistry.addShapelessRecipe(new ItemStack(Activator.blocks.rfActivator), Activator.blocks.redstoneActivator, Activator.blocks.redstoneActivator, Activator.blocks.redstoneActivator, redCrystal, redCrystal, redCrystal, coil, coil, coil);
		}
	}

}

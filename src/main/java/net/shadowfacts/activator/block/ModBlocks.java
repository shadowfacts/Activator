package net.shadowfacts.activator.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author shadowfacts
 */
public class ModBlocks {

	public BlockActivator activator;
	public BlockRedstoneActivator redstoneActivator;
	public BlockRFActivator rfActivator;

	public void initializeBlocks() {
		activator = register(new BlockActivator(), "activator");
		redstoneActivator = register(new BlockRedstoneActivator(), "activator-redstone");
		rfActivator = register(new BlockRFActivator(), "activator-rf");
	}

	private <T extends Block> T register(T block, String name) {
		GameRegistry.registerBlock(block, name);
		return block;
	}

}

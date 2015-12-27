package net.shadowfacts.activator.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * @author shadowfacts
 */
public class ModBlocks {

	public BlockActivator activator;

	public void initializeBlocks() {
		activator = register(new BlockActivator(), "activator");
	}

	private <T extends Block> T register(T block, String name) {
		GameRegistry.registerBlock(block, name);
		return block;
	}

	private <T extends Block> T register(T block, String name, Class<? extends ItemBlock> itemBlockClass, Object... itemBlockCtorArgs) {
		GameRegistry.registerBlock(block, itemBlockClass, name, itemBlockCtorArgs);
		return block;
	}

}

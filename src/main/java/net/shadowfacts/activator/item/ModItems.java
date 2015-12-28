package net.shadowfacts.activator.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * @author shadowfacts
 */
public class ModItems {

	public BaseItem gear;

	public void initializeItems() {
		gear = register(new BaseItem("gear", "gear"), "gear");
	}

	private <T extends Item> T register(T item, String name) {
		GameRegistry.registerItem(item, name);
		return item;
	}

}

package net.shadowfacts.activator.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author shadowfacts
 */
public class ModItems {

	public BaseItem gear;

	public void initializeItems() {
		gear = register(new BaseItem("gear"), "gear");
	}

	private <T extends Item> T register(T item, String name) {
		GameRegistry.registerItem(item, name);
		return item;
	}

}

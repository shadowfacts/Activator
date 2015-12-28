package net.shadowfacts.activator.item;

import net.minecraft.item.Item;

/**
 * @author shadowfacts
 */
public class BaseItem extends Item {

	public BaseItem(String unlocName, String textureName) {
		setUnlocalizedName(unlocName);
		setTextureName(textureName);
	}

}

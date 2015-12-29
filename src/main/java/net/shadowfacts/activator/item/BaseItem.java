package net.shadowfacts.activator.item;

import net.minecraft.item.Item;
import net.shadowfacts.activator.Activator;

/**
 * @author shadowfacts
 */
public class BaseItem extends Item {

	public BaseItem(String unlocName, String textureName) {
		setUnlocalizedName(unlocName);
		setTextureName(Activator.modId + ":" + textureName);
	}

}

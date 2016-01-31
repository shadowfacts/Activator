package net.shadowfacts.activator.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author shadowfacts
 */
public class BaseItem extends Item {

	public BaseItem(String unlocName) {
		setUnlocalizedName(unlocName);
		setCreativeTab(CreativeTabs.tabMisc);
	}

}

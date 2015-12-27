package net.shadowfacts.activator.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.shadowfacts.activator.achievement.AchievementProvider;

/**
 * @author shadowfacts
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void itemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack result = event.crafting;
		if (result.getItem() instanceof AchievementProvider) {
			((AchievementProvider)result.getItem()).grantAchievement(event.player);
		} else if (result.getItem() instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock)result.getItem();
			if (itemBlock.field_150939_a instanceof AchievementProvider) {
				((AchievementProvider)itemBlock.field_150939_a).grantAchievement(event.player);
			}
		}
	}

	@SubscribeEvent
	public void itemSmelted(PlayerEvent.ItemSmeltedEvent event) {
		ItemStack result = event.smelting;
		if (result.getItem() instanceof AchievementProvider) {
			((AchievementProvider)result.getItem()).grantAchievement(event.player);
		} else if (result.getItem() instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock)result.getItem();
			if (itemBlock.field_150939_a instanceof AchievementProvider) {
				((AchievementProvider)itemBlock.field_150939_a).grantAchievement(event.player);
			}
		}
	}

}

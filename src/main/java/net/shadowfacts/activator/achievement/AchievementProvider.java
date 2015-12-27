package net.shadowfacts.activator.achievement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;

/**
 * @author shadowfacts
 */
public interface AchievementProvider {

	Achievement getAchievement();

	default void grantAchievement(EntityPlayer player) {
		player.addStat(getAchievement(), 1);
	}

}

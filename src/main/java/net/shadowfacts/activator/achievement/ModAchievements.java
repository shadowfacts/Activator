package net.shadowfacts.activator.achievement;

import net.minecraftforge.common.AchievementPage;
import net.shadowfacts.activator.Activator;

/**
 * @author shadowfacts
 */
public class ModAchievements {

	public static AchievementPage page;

	public static void registerAchievements() {
		Activator.log.info("Registering achievements");


		page = new AchievementPage("Activator");
		AchievementPage.registerAchievementPage(page);

	}

}

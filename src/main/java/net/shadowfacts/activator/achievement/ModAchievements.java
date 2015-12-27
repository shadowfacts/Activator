package net.shadowfacts.activator.achievement;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.shadowfacts.activator.Activator;

/**
 * @author shadowfacts
 */
public class ModAchievements {

	public static AchievementPage page;

	public static Achievement craftActivator;
	public static Achievement craftRedstoneActivator;

	public static void registerAchievements() {
		Activator.log.info("Registering achievements");


		craftActivator = new Achievement("craftActivator", "craftActivator", 0, 0, Activator.blocks.activator, null)
				.initIndependentStat().registerStat();

		craftRedstoneActivator = new Achievement("craftRedstoneActivator", "craftRedstoneActivator", 0, 0, Activator.blocks.redstoneActivator, craftActivator);


		page = new AchievementPage("Activator", craftActivator, craftRedstoneActivator);
		AchievementPage.registerAchievementPage(page);

	}

}

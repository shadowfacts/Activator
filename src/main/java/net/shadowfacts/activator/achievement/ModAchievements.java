package net.shadowfacts.activator.achievement;

import net.minecraft.block.Block;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.shadowmc.achievement.ShadowAchievement;

/**
 * @author shadowfacts
 */
public class ModAchievements {

	public static AchievementPage page;

	public static Achievement craftActivator;
	public static Achievement craftRedstoneActivator;
	public static Achievement craftRFActivator;

	public static void registerAchievements() {
		Activator.log.info("Registering achievements");

//		craftActivator = new ShadowAchievement("craftActivator", "craftActivator", 0, 0, Activator.blocks.activator);
		craftActivator = new ShadowAchievement("craftActivator", "craftActivator", 0, 0, (Block)null);

//		craftRedstoneActivator = new ShadowAchievement("craftRedstoneActivator", "craftRedstoneActivator", 5, 0, Activator.blocks.redstoneActivator, craftActivator);
		craftRedstoneActivator = new ShadowAchievement("craftRedstoneActivator", "craftRedstoneActivator", 5, 0, (Block)null, craftActivator);

//		craftRFActivator = new ShadowAchievement("craftRFActivator", "craftRFActivator", 5, 5, Activator.blocks.rfActivator, craftActivator);
		craftRFActivator = new ShadowAchievement("craftRFActivator", "craftRFActivator", 5, 5, (Block)null, craftActivator);

		page = new AchievementPage("Activator", craftActivator, craftRedstoneActivator, craftRFActivator);
		AchievementPage.registerAchievementPage(page);
	}

}

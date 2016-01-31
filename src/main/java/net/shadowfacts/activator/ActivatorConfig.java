package net.shadowfacts.activator;

import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigProperty;

/**
 * @author shadowfacts
 */
@Config(name = "Activator")
public class ActivatorConfig {

	@ConfigProperty(name = "enabled", category = "activator.basic", comment = "Enable the basic activator")
	public static boolean basicEnabled = true;

	@ConfigProperty(name = "enabled", category = "activator.redstone", comment = "Enable the redstone activator")
	public static boolean redstoneEnabled = true;

	@ConfigProperty(name = "enabled", category = "activator.rf", comment = "Enable the RF activator")
	public static boolean rfEnabled = true;

	@ConfigProperty(name = "capacity", category = "activator.rf", comment = "How much RF the activator can store")
	public static int rfCapacity = 2048;

	@ConfigProperty(name = "attackEnergy", category = "activator.rf", comment = "How much RF required to attack an entity")
	public static int rfAttackEnergy = 512;

	@ConfigProperty(name = "breakEnergy", category = "activator.rf", comment = "How much RF required to break a block")
	public static int rfBreakEnergy = 512;

	@ConfigProperty(name = "rightClickEnergy", category = "activator.rf", comment = "How much RF required to right-click")
	public static int rfRightClickEnergy = 256;

	@ConfigProperty(name = "enabled", category = "activator.eu", comment = "Enable the EU activator")
	public static boolean euEnabled = true;

	@ConfigProperty(name = "capacity", category = "activator.eu", comment = "How much EU the activator can store")
	public static int euCapacity = 2048;

	@ConfigProperty(name = "attackEnergy", category = "activator.eu", comment = "How much EU required to attack an entity")
	public static int euAttackEnergy = 512;

	@ConfigProperty(name = "breakEnergy", category = "activator.eu", comment = "How much EU required to break a block")
	public static int euBreakEnergy = 512;

}

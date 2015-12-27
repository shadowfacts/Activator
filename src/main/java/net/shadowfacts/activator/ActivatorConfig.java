package net.shadowfacts.activator;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigManager;

/**
 * @author shadowfacts
 */
@Config(name = "Activator")
public class ActivatorConfig {

	public static void initialize(FMLPreInitializationEvent event) {
		Activator.log.info("Loading config");
		ConfigManager.instance.configDirPath = event.getModConfigurationDirectory().getAbsolutePath();
		ConfigManager.instance.register(Activator.modId, ActivatorConfig.class);
		ConfigManager.instance.load(Activator.modId);
	}

}

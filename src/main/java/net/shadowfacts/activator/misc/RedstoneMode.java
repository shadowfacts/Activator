package net.shadowfacts.activator.misc;

import net.shadowfacts.shadowmc.util.StringHelper;

/**
 * Redstone activation modes
 *
 * @author shadowfacts
 */
public enum RedstoneMode {

	ALWAYS("redstone.always"),
	NEVER("redstone.never"),
	HIGH("redstone.high"),
	LOW("redstone.low"),
	PULSE("redstone.pulse");

	private String unlocalizedName;

	RedstoneMode(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	public String getLocalizedName() {
		return StringHelper.localize(unlocalizedName);
	}

	public static RedstoneMode get(int i) {
		return values()[i];
	}

}

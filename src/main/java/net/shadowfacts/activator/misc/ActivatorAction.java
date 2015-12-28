package net.shadowfacts.activator.misc;

import net.shadowfacts.shadowmc.util.StringHelper;

/**
 * @author shadowfacts
 */
public enum ActivatorAction {

	LEFT_CLICK("activator.action.left"),
	RIGHT_CLICK("activator.action.right");

	private String unlocalizedName;

	ActivatorAction(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	public String getLocalizedName() {
		return StringHelper.localize(unlocalizedName);
	}

	public static ActivatorAction get(int i) {
		return values()[i];
	}

}

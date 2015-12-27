package net.shadowfacts.activator.misc;

import net.shadowfacts.shadowmc.util.StringHelper;

/**
 * @author shadowfacts
 */
public enum ActivatorAction {

	LEFT_CLICK("activator.action.left"),
//	SNEAK_LEFT_CLICK("activator.action.left_sneak"),
	RIGHT_CLICK("activator.action.right");
//	SNEAK_RIGHT_CLICK("activator.action.right_sneak");

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

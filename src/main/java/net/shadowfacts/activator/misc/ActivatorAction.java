package net.shadowfacts.activator.misc;

import net.shadowfacts.shadowmc.util.StringHelper;

/**
 * @author shadowfacts
 */
public enum ActivatorAction {

	RIGHT_CLICK("activator.action.right"),
	LEFT_CLICK("activator.action.left");

	private String unlocalized;

	ActivatorAction(String unlocalized) {
		this.unlocalized = unlocalized;
	}

	public String getLocalizedName() {
		return StringHelper.localize(unlocalized);
	}

}

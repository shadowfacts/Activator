package net.shadowfacts.activator.gui.component;

import net.shadowfacts.activator.gui.BaseGuiContainer;
import net.shadowfacts.activator.misc.ActivatorAction;

/**
 * @author shadowfacts
 */
public class GuiButtonActionType extends BaseGuiButton {

	public ActivatorAction action;

	public GuiButtonActionType(int id, int x, int y, int width, int height, BaseGuiContainer owner, String tooltip, ActivatorAction action) {
		super(id, x, y, width, height, action.getLocalizedName(), owner, tooltip);
		this.action = action;
	}

	public void onClick() {
		updateAction(getNextAction());
	}

	private void updateAction(ActivatorAction action) {
		this.action = action;
		this.displayString = action.getLocalizedName();
	}

	private ActivatorAction getNextAction() {
		int current = action.ordinal();
		int max = ActivatorAction.values().length - 1;
		int newAction = current + 1 > max ? 0 : current + 1;
		return ActivatorAction.get(newAction);
	}

}

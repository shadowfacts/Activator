package net.shadowfacts.activator.gui.component;

import cofh.api.energy.IEnergyHandler;
import net.shadowfacts.shadowmc.gui.component.GUIVerticalBarIndicator;
import net.shadowfacts.shadowmc.util.Color;

import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public class GUIRFIndicator extends GUIVerticalBarIndicator {

	protected IEnergyHandler handler;

	public GUIRFIndicator(int x, int y, IEnergyHandler handler) {
		super(x, y, 15, 75, null);
		this.handler = handler;
		levelSupplier = this::getRFLevel;
		primaryColor = Color.PURE_RED;
		secondaryColor = Color.DARK_RED;
	}

	private float getRFLevel() {
		return handler.getEnergyStored(null) / (float)handler.getMaxEnergyStored(null);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);
	}

	@Override
	public List<String> getTooltip() {
		return Collections.singletonList(String.format("%d / %d RF", handler.getEnergyStored(null), handler.getMaxEnergyStored(null)));
	}
}

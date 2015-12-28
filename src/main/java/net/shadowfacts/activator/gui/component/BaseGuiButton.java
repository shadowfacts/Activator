package net.shadowfacts.activator.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.shadowfacts.activator.gui.BaseGuiContainer;
import net.shadowfacts.shadowmc.util.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public class BaseGuiButton extends GuiButton {

	protected List<String> tooltip = new ArrayList<>();
	protected BaseGuiContainer owner;

	public BaseGuiButton(int id, int x, int y, String text, BaseGuiContainer owner, String tooltip) {
		super(id, x, y, text);
		this.owner = owner;
		this.tooltip = Collections.singletonList(StringHelper.localize(tooltip));
	}

	public BaseGuiButton(int id, int x, int y, int width, int height, String text, BaseGuiContainer owner, String tooltip) {
		super(id, x, y, width, height, text);
		this.owner = owner;
		this.tooltip = Collections.singletonList(StringHelper.localize(tooltip));
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= xPosition && mouseX <= xPosition + width &&
				mouseY >= yPosition && mouseY <= yPosition + height;
	}

	protected void drawHoveringText(List<String> text, int x, int y, int color) {
		owner.drawTooltip(text, x, y, color);
	}

	protected void drawHoveringText(List<String> text, int x, int y) {
		drawHoveringText(text, x, y, 0xffffff);
	}

	public void drawTooltip(int x, int y) {
		drawHoveringText(getTooltip(), x, y);
	}

	protected List<String> getTooltip() {
		return tooltip;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);
	}

}

package net.shadowfacts.activator.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.shadowfacts.activator.gui.BaseGuiContainer;

import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public abstract class BaseGuiComponent extends Gui {

	protected Minecraft mc;

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	protected BaseGuiContainer owner;
	protected List<String> tooltip;

	protected boolean visible = true;

	public BaseGuiComponent(Minecraft mc, int x, int y, int width, int height, BaseGuiContainer owner, List<String> tooltip) {
		this.mc = mc;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.owner = owner;
		this.tooltip = tooltip;
	}

	public BaseGuiComponent(Minecraft mc, int x, int y, int width, int height, BaseGuiContainer owner, String tooltip) {
		this(mc, x, y, width, height, owner, Collections.singletonList(tooltip));
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX < x + width &&
				mouseY >= y && mouseY < y + height;
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

	public boolean isVisible(int mouseX, int mouseY) {
		return visible;
	}

	public void draw(int mouseX, int mouseY) {
		draw();
		if (isHovered(mouseX, mouseY)) drawTooltip(mouseX, mouseY);
	}

	protected abstract void draw();

}

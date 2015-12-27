package net.shadowfacts.activator.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.shadowfacts.activator.gui.BaseGuiContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public abstract class BaseGuiButton extends GuiButton {

	protected List<String> tooltip = new ArrayList<>();
	protected BaseGuiContainer owner;

	public BaseGuiButton(int id, int x, int y, String text, BaseGuiContainer owner) {
		super(id, x, y, text);
		this.owner = owner;
	}

	public BaseGuiButton(int id, int x, int y, int width, int height, String text, BaseGuiContainer owner) {
		super(id, x, y, width, height, text);
		this.owner = owner;
	}

	protected boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= xPosition && mouseX <= xPosition + width &&
				mouseY >= yPosition && mouseY <= yPosition + height;
	}

	protected void drawHoveringText(List<String> text, int x, int y, int color) {
		owner.drawTooltip(text, x, y, color);
	}

	protected void drawHoveringText(List<String> text, int mouseX, int mouseY) {
		drawHoveringText(text, mouseX, mouseY, 0xffffff);
	}

	protected void draw(Minecraft mc, int mouseX, int mouseY) {

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);
		draw(mc, mouseX, mouseY);
		if (isHovered(mouseX, mouseY)) {
			drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

	@Override
	public void func_146111_b(int mouseX, int mouseY) {
		if (isHovered(mouseX, mouseY)) {
			drawHoveringText(tooltip, mouseX, mouseY);
		}
	}
}

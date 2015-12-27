package net.shadowfacts.activator.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.gui.BaseGuiContainer;

import java.util.Collections;

/**
 * @author shadowfacts
 */
public class GuiButtonToggle extends BaseGuiButton {

	private static final ResourceLocation texture = new ResourceLocation("activator" ,"textures/gui/toggle.png");

	public boolean state;

	public GuiButtonToggle(int id, int x, int y, BaseGuiContainer owner, String tooltip, boolean state) {
		super(id, x, y, 20, 20, null, owner, tooltip);
		this.state = state;
	}

	public void onClick() {
		state = !state;
	}

	@Override
	protected void draw(Minecraft mc, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(texture);
		if (state) { // on
			drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
		} else { // off
			drawTexturedModalRect(xPosition, yPosition, 20, 0, width, height);
		}
	}

}

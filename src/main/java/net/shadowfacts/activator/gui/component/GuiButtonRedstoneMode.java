package net.shadowfacts.activator.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.gui.BaseGuiContainer;
import net.shadowfacts.activator.misc.RedstoneMode;
import net.shadowfacts.shadowmc.util.StringHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @author shadowfacts
 */
public class GuiButtonRedstoneMode extends BaseGuiButton {

	private static final ResourceLocation texture = new ResourceLocation(Activator.modId, "textures/gui/redstonemode.png");

	public RedstoneMode mode;

	public GuiButtonRedstoneMode(int id, int x, int y, BaseGuiContainer owner, RedstoneMode mode) {
		super(id, x, y, 20, 20, null, owner, null);
		this.mode = mode;
	}

	public GuiButtonRedstoneMode(int id, int x, int y, BaseGuiContainer owner) {
		this(id, x, y, owner, RedstoneMode.HIGH);
	}

	public void onClick() {
		mode = getNextMode();
	}

	private RedstoneMode getNextMode() {
		int current = mode.ordinal();
		int max = RedstoneMode.values().length - 1;
		int newMode = current + 1 > max ? 0 : current + 1;
		return RedstoneMode.get(newMode);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		super.drawButton(mc, mouseX, mouseY);
//		GL11.glPushMatrix();
//
//		GL11.glTranslatef(68, 26, 0);
//		GL11.glScalef(0.75f, 0.75f, 0.75f);

//		TODO: Fix texture (scale image to 512^2, 

		mc.getTextureManager().bindTexture(texture);
		switch (mode) {
			case ALWAYS:
//				drawTexturedModalRect(xPosition, yPosition, 84, 0, width, height);
				drawTexturedModalRect(xPosition, yPosition, 42, 0, width, height);
				break;
			case NEVER:
//				drawTexturedModalRect(xPosition, yPosition, 124, 0, width, height);
				drawTexturedModalRect(xPosition, yPosition, 62, 0, width, height);
				break;
			case HIGH:
//				drawTexturedModalRect(xPosition, yPosition, 42, 0, width, height);
				drawTexturedModalRect(xPosition, yPosition, 21, 0, width, height);
				break;
			case LOW:
//				drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
				drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
				break;
			case PULSE:
//				drawTexturedModalRect(xPosition, yPosition, 164, 0, width, height);
				drawTexturedModalRect(xPosition, yPosition, 82, 0, width, height);
				break;
		}

//		GL11.glPopMatrix();
	}

	@Override
	protected List<String> getTooltip() {
		return Arrays.asList(StringHelper.localize("gui.activator.redstone"),
				StringHelper.localize("gui.activator.redstone2") + " " + mode.getLocalizedName());
	}
}

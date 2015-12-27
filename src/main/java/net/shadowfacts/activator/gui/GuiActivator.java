package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.gui.component.GuiButtonActionType;
import net.shadowfacts.activator.gui.component.GuiButtonToggle;
import net.shadowfacts.activator.misc.KeyboardHelper;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.shadowmc.util.StringHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author shadowfacts
 */
public class GuiActivator extends BaseGuiContainer {

	private static final ResourceLocation bgTexture = new ResourceLocation(Activator.modId, "textures/gui/activator.png");

	private TileEntityActivator activator;

	private GuiButtonActionType actionSelector;
	private GuiButtonToggle sneaking;

	public GuiActivator(InventoryPlayer playerInv, TileEntityActivator activator) {
		super(new ContainerActivator(playerInv, activator));
		this.activator = activator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();

		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		buttonList.add(new GuiButton(0, cornerX + 20, cornerY + 35, 20, 20, "-"));
		buttonList.add(new GuiButton(1, cornerX + xSize - 40, cornerY + 35, 20, 20, "+"));
		actionSelector = new GuiButtonActionType(2, cornerX + (xSize / 2) - 50, cornerY + 60, 100, 20, this, activator.action);
		sneaking = new GuiButtonToggle(3, cornerX, cornerY, this, activator.sneaking, StringHelper.localize("gui.activator.sneak"));
		buttonList.add(actionSelector);
		buttonList.add(sneaking);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 0:
				activator.decreaseActivateFrequency(KeyboardHelper.isShiftPressed() ? 10 : 5);
				break;
			case 1:
				activator.increaseActivateFrequency(KeyboardHelper.isShiftPressed() ? 10 : 5);
				break;
			case 2:
				actionSelector.onClick();
				activator.action = actionSelector.action;
				break;
			case 3:
				sneaking.onClick();
				activator.sneaking = sneaking.state;
				break;
		}

		activator.sync();
	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int cornerX = (width - xSize) / 2;
		int cornerY = (height - ySize) / 2;

		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(bgTexture);
		drawTexturedModalRect(cornerX, cornerY, 0, 0, xSize, ySize);


		String s = String.format("%d ticks", activator.activateFrequency);
		drawString(mc.fontRenderer, s, cornerX + (xSize / 2) - (mc.fontRenderer.getStringWidth(s) / 2), cornerY + 41, 0xffffff);
	}

}

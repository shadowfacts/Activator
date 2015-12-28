package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.gui.component.BaseGuiButton;
import net.shadowfacts.activator.gui.component.GuiButtonActionType;
import net.shadowfacts.activator.gui.component.GuiButtonRedstoneMode;
import net.shadowfacts.activator.gui.component.GuiButtonToggle;
import net.shadowfacts.activator.misc.KeyboardHelper;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;
import org.lwjgl.opengl.GL11;

/**
 * @author shadowfacts
 */
public class GuiRedstoneActivator extends BaseGuiContainer {

	private static final ResourceLocation bgTexture = new ResourceLocation(Activator.modId, "textures/gui/activator.png");

	private TileEntityRedstoneActivator activator;

	private GuiButtonActionType actionSelector;
	private GuiButtonToggle sneaking;
	private GuiButtonRedstoneMode redstoneMode;

	public GuiRedstoneActivator(InventoryPlayer playerInv, TileEntityRedstoneActivator activator) {
		super(new ContainerActivator(playerInv, activator));
		this.activator = activator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();

		buttonList.add(new BaseGuiButton(0, guiLeft + 20, guiTop + 35, 20, 20, "-", this, "gui.activator.freq.decrease"));
		buttonList.add(new BaseGuiButton(1, guiLeft + xSize - 40, guiTop + 35, 20, 20, "+", this, "gui.activator.freq.increase"));

		actionSelector = new GuiButtonActionType(2, guiLeft + 20, guiTop + 60, 60, 20, this, "gui.activator.action", activator.action);
		sneaking = new GuiButtonToggle(3, guiLeft + xSize - 80, guiTop + 60, this, "gui.activator.sneak", activator.sneaking);
		redstoneMode = new GuiButtonRedstoneMode(4, guiLeft + xSize - 40, guiTop + 60, this, activator.redstoneMode);

		buttonList.add(actionSelector);
		buttonList.add(sneaking);
		buttonList.add(redstoneMode);
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
			case 4:
				redstoneMode.onClick();
				activator.redstoneMode = redstoneMode.mode;
				break;
		}

		activator.sync();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(bgTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		String s = String.format("%d ticks", activator.activateFrequency);
		drawString(mc.fontRenderer, s, guiLeft + (xSize / 2) - (mc.fontRenderer.getStringWidth(s) / 2), guiTop + 41, 0xffffff);
	}
}

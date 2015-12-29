package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.gui.component.*;
import net.shadowfacts.activator.misc.KeyboardHelper;
import net.shadowfacts.activator.network.PacketRequestTEUpdate;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import org.lwjgl.opengl.GL11;

/**
 * @author shadowfacts
 */
public class GuiRFActivator extends BaseGuiContainer {

	private static final ResourceLocation bgTexture = new ResourceLocation(Activator.modId, "textures/gui/activator.png");

	private TileEntityRFActivator activator;

	private GuiButtonActionType actionSelector;
	private GuiButtonToggle sneaking;
	private GuiButtonRedstoneMode redstoneMode;
	private GuiComponentRFIndicator rfIndicator;

	private int ticks = 0;

	public GuiRFActivator(InventoryPlayer playerInv, TileEntityRFActivator activator) {
		super(new ContainerActivator(playerInv, activator));
		this.activator = activator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();

		rfIndicator = new GuiComponentRFIndicator(mc, guiLeft + 7, guiTop + 5, this, activator);
		components.add(rfIndicator);

		buttonList.add(new BaseGuiButton(0, guiLeft + 30, guiTop + 35, 20, 20, "-", this, "gui.activator.freq.decrease"));
		buttonList.add(new BaseGuiButton(1, guiLeft + xSize - 40, guiTop + 35, 20, 20, "+", this, "gui.activator.freq.increase"));

		actionSelector = new GuiButtonActionType(2, guiLeft + 30, guiTop + 60, 60, 20, this, "gui.activator.action", activator.action);
		sneaking = new GuiButtonToggle(3, guiLeft + xSize - 73, guiTop + 60, this, "gui.activator.sneak", activator.sneaking);
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
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		super.drawScreen(mouseX, mouseY, partialTick);

		if (rfIndicator.isHovered(mouseX, mouseY)) rfIndicator.drawTooltip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GL11.glColor4f(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(bgTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, width, height);

		String s = String.format("%d ticks", activator.activateFrequency);
		drawString(mc.fontRenderer, s, guiLeft + (xSize / 2) - (mc.fontRenderer.getStringWidth(s) / 2), guiTop + 41, 0xffffff);
//		TODO: fix string position

		if (ticks % 40 == 0) Activator.network.sendToServer(new PacketRequestTEUpdate(activator));
		rfIndicator.draw();
		ticks++;
	}

}

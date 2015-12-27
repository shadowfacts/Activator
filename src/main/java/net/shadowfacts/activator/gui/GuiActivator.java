package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.gui.component.BaseGuiButton;
import net.shadowfacts.activator.gui.component.GuiButtonActionType;
import net.shadowfacts.activator.gui.component.GuiButtonToggle;
import net.shadowfacts.activator.misc.KeyboardHelper;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.shadowmc.util.StringHelper;
import org.lwjgl.opengl.GL11;

import java.util.stream.Stream;

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

		buttonList.add(new BaseGuiButton(0, cornerX + 20, cornerY + 35, 20, 20, "-", this, "gui.activator.freq.decrease"));
		buttonList.add(new BaseGuiButton(1, cornerX + xSize - 40, cornerY + 35, 20, 20, "+", this, "gui.activator.freq.increase"));
		actionSelector = new GuiButtonActionType(2, cornerX + 20, cornerY + 60, 100, 20, this, "gui.activator.action", activator.action);
		sneaking = new GuiButtonToggle(3, cornerX + xSize - 40, cornerY + 60, this, "gui.activator.sneak", activator.sneaking);
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
	@SuppressWarnings("unchecked")
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		super.drawScreen(mouseX, mouseY, partialTick);

		((Stream<GuiButton>)buttonList.stream())
				.filter(button -> button instanceof BaseGuiButton)
				.map(button -> (BaseGuiButton)button)
				.filter(button -> button.isHovered(mouseX, mouseY))
				.forEach(button -> button.drawTooltip(mouseX, mouseY));
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

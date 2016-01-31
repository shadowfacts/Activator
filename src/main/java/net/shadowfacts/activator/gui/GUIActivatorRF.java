package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.gui.component.GUIRFIndicator;
import net.shadowfacts.activator.misc.ActivatorAction;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import net.shadowfacts.shadowmc.ShadowMC;
import net.shadowfacts.shadowmc.gui.component.GUIComponentText;
import net.shadowfacts.shadowmc.gui.component.GUIComponentTexture;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonEnum;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonRedstoneMode;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonText;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonToggle;
import net.shadowfacts.shadowmc.gui.mcwrapper.GuiContainerWrapper;
import net.shadowfacts.shadowmc.gui.mcwrapper.MCBaseGUIContainer;
import net.shadowfacts.shadowmc.network.PacketRequestTEUpdate;
import net.shadowfacts.shadowmc.util.*;

/**
 * @author shadowfacts
 */
public class GUIActivatorRF extends MCBaseGUIContainer {

	private TileEntityRFActivator activator;

	private GUIComponentTexture bg;
	private GUIRFIndicator rfIndicator;
	private GUIComponentText activationFreq;
	private GUIButtonText decrease;
	private GUIButtonText increase;
	private GUIButtonEnum<ActivatorAction> action;
	private GUIButtonToggle sneaking;
	private GUIButtonRedstoneMode redstoneMode;

	private int xSize = 176;
	private int ySize = 166;
	private int guiLeft = 0;
	private int guiTop = 0;

	private int ticks;

	public GUIActivatorRF(GuiContainerWrapper wrapper, TileEntityRFActivator activator) {
		super(wrapper);
		this.activator = activator;

		bg = addChild(new GUIComponentTexture(guiLeft, guiTop, xSize, ySize, GUIActivator.bgTexture));

		rfIndicator = addChild(new GUIRFIndicator(guiLeft + 7, guiTop + 5, activator));

		activationFreq = addChild(new GUIComponentText(guiLeft + (xSize / 2) - (mc.fontRendererObj.getStringWidth("20 ticks") / 2), guiTop + 41, "20 ticks"));
		activationFreq.setColor(new Color(4210752));

		decrease = addChild(new GUIButtonText(guiLeft + 30, guiTop + 35, 20, 20, this::decreaseFreq, "-"));
		decrease.addTooltip(StringHelper.localize("gui.activator.freq.decrease"));
		increase = addChild(new GUIButtonText(guiLeft + xSize - 40, guiTop + 35, 20, 20, this::increaseFreq, "+"));
		increase.addTooltip(StringHelper.localize("gui.activator.freq.increase"));

		action = addChild(new GUIButtonEnum<>(guiLeft + 30, guiTop + 60, 60, 20, activator.getAction(), ActivatorAction::getLocalizedName, this::changeAction));
		action.addTooltip(StringHelper.localize("gui.activator.action"));
		sneaking = addChild(new GUIButtonToggle(guiLeft + xSize - 73, guiTop + 60, activator.sneaking, this::changeSneaking));
		sneaking.addTooltip(StringHelper.localize("gui.activator.sneak"));
		redstoneMode = addChild(new GUIButtonRedstoneMode(guiLeft + xSize - 40, guiTop + 60, activator.redstoneMode, this::changeRedstoneMode));


		updateActivationFreq();
	}

	@Override
	public void setInitialized(boolean initialized) {
		super.setInitialized(initialized);
		if (initialized) {
			guiLeft = (width - xSize) / 2;
			guiTop = (height - ySize) / 2;

			updatePosition(guiLeft, guiTop);
		}
	}

	@Override
	public void drawTooltip(int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(-guiLeft, -guiTop, 0); // IDEK
		super.drawTooltip(x, y);
		GlStateManager.popMatrix();
	}

	private void updateActivationFreq() {
		String text = activator.activationFrequency + " ticks";
		activationFreq.setText(text);
		activationFreq.setX(guiLeft + (xSize / 2) - (mc.fontRendererObj.getStringWidth(text) / 2));

		if (activator.activationFrequency == TileEntityActivator.MIN_UPDATE_FREQ) {
			decrease.setEnabled(false);
			increase.setEnabled(true);
		} else if (activator.activationFrequency == TileEntityActivator.MAX_UPDATE_FREQ) {
			decrease.setEnabled(true);
			increase.setEnabled(false);
		} else {
			decrease.setEnabled(true);
			increase.setEnabled(true);
		}
	}

	private boolean decreaseFreq(GUIButtonText button, MouseButton mouseButton) {
		activator.decreaseActivationFrequency(KeyboardHelper.isShiftPressed() ? 10 : 5);
		activator.sync();
		updateActivationFreq();
		return true;
	}

	private boolean increaseFreq(GUIButtonText button, MouseButton mouseButton) {
		activator.increaseActivationFrequency(KeyboardHelper.isShiftPressed() ? 10 : 5);
		activator.sync();
		updateActivationFreq();
		return true;
	}

	private void changeAction(GUIButtonEnum<ActivatorAction> button) {
		activator.setAction(button.getValue());
		activator.sync();
	}

	private void changeSneaking(GUIButtonToggle button) {
		activator.sneaking = button.state;
		activator.sync();
	}

	private void changeRedstoneMode(RedstoneMode mode) {
		activator.redstoneMode = mode;
		activator.sync();
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		if (ticks % 40 == 0) ShadowMC.network.sendToServer(new PacketRequestTEUpdate(activator));
		ticks++;

		super.draw(mouseX, mouseY);
	}

	public static GuiScreen create(InventoryPlayer playerInv, TileEntityRFActivator activator) {
		GuiContainerWrapper wrapper = new GuiContainerWrapper(new ContainerActivator(playerInv, activator));
		GUIActivatorRF gui = new GUIActivatorRF(wrapper, activator);
		gui.setZLevel(0);
		wrapper.gui = gui;
		return wrapper;
	}

}

package net.shadowfacts.activator.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.misc.ActivatorAction;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.shadowmc.gui.component.GUIComponentText;
import net.shadowfacts.shadowmc.gui.component.GUIComponentTexture;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonEnum;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonText;
import net.shadowfacts.shadowmc.gui.component.button.GUIButtonToggle;
import net.shadowfacts.shadowmc.gui.mcwrapper.GuiContainerWrapper;
import net.shadowfacts.shadowmc.gui.mcwrapper.MCBaseGUIContainer;
import net.shadowfacts.shadowmc.util.Color;
import net.shadowfacts.shadowmc.util.KeyboardHelper;
import net.shadowfacts.shadowmc.util.MouseButton;
import net.shadowfacts.shadowmc.util.StringHelper;

/**
 * @author shadowfacts
 */
public class GUIActivator extends MCBaseGUIContainer {

	public static final ResourceLocation bgTexture = new ResourceLocation(Activator.modId, "textures/gui/activator.png");

	private TileEntityActivator activator;

	private GUIComponentTexture bg;
	private GUIComponentText activationFreq;
	private GUIButtonText decrease;
	private GUIButtonText increase;
	private GUIButtonEnum<ActivatorAction> action;
	private GUIButtonToggle sneaking;

	private int xSize = 176;
	private int ySize = 166;
	private int guiLeft = 0;
	private int guiTop = 0;

	public GUIActivator(GuiContainerWrapper wrapper, TileEntityActivator activator) {
		super(wrapper);
		this.activator = activator;

		int xSize = 176;
		int ySize = 166;
		int guiLeft = 0;
		int guiTop = 0;

		bg = addChild(new GUIComponentTexture(guiLeft, guiTop, 176, 166, bgTexture));
		activationFreq = addChild(new GUIComponentText(guiLeft + (xSize / 2) - (mc.fontRendererObj.getStringWidth("20 ticks") / 2), guiTop + 41, "20 ticks"));
		activationFreq.setColor(new Color(4210752));
		decrease = addChild(new GUIButtonText(guiLeft + 20, guiTop + 35, 20, 20, this::decreaseFreq, "-"));
		decrease.addTooltip(StringHelper.localize("gui.activator.freq.decrease"));
		increase = addChild(new GUIButtonText(guiLeft + xSize - 40, guiTop + 35, 20, 20, this::increaseFreq, "+"));
		increase.addTooltip(StringHelper.localize("gui.activator.freq.increase"));
		action = addChild(new GUIButtonEnum<>(guiLeft + 20, guiTop + 60, 100, 20, activator.getAction(), ActivatorAction::getLocalizedName, this::changeAction));
		action.addTooltip(StringHelper.localize("gui.activator.action"));
		sneaking = addChild(new GUIButtonToggle(guiLeft + xSize - 40, guiTop + 60, activator.sneaking, this::changeSneaking));
		sneaking.addTooltip(StringHelper.localize("gui.activator.sneak"));

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

	public static GuiScreen create(InventoryPlayer playerInv, TileEntityActivator activator) {
		GuiContainerWrapper wrapper = new GuiContainerWrapper(new ContainerActivator(playerInv, activator));
		GUIActivator gui = new GUIActivator(wrapper, activator);
		gui.setZLevel(0);
		wrapper.gui = gui;
		return wrapper;
	}

}

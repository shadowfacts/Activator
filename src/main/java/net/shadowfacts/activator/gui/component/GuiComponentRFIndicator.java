package net.shadowfacts.activator.gui.component;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.shadowfacts.activator.gui.BaseGuiContainer;

import java.util.Collections;
import java.util.List;

/**
 * @author shadowfacts
 */
public class GuiComponentRFIndicator extends BaseGuiComponent {

	private static final ResourceLocation texture = new ResourceLocation("activator", "textures/gui/rfindicator.png");

	protected IEnergyHandler handler;

	public GuiComponentRFIndicator(Minecraft mc, int x, int y, BaseGuiContainer owner, IEnergyHandler handler) {
		super(mc, x, y, 15, 75, owner, (String)null);
		this.handler = handler;
	}

	@Override
	public void draw() {
		mc.getTextureManager().bindTexture(texture);
		int rf = (int)((handler.getEnergyStored(ForgeDirection.UNKNOWN) / (float)handler.getMaxEnergyStored(ForgeDirection.UNKNOWN)) * 75);
		drawTexturedModalRect(x, y, 0, 0, width, height);
		drawTexturedModalRect(x, y + height - rf, 15, 0, width, rf);
	}

	@Override
	protected List<String> getTooltip() {
		return Collections.singletonList(String.format("%d / %d RF", handler.getEnergyStored(ForgeDirection.UNKNOWN), handler.getMaxEnergyStored(ForgeDirection.UNKNOWN)));
	}

}

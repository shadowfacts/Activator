package net.shadowfacts.activator.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;

/**
 * @author shadowfacts
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUI.values()[ID]) {
			case BASIC:
				return new ContainerActivator(player.inventory, (TileEntityActivator)world.getTileEntity(x, y, z));
			case REDSTONE:
				return new ContainerActivator(player.inventory, (TileEntityActivator) world.getTileEntity(x, y, z));
			case RF:
				return new ContainerActivator(player.inventory, (TileEntityActivator)world.getTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUI.values()[ID]) {
			case BASIC:
				return new GuiActivator(player.inventory, (TileEntityActivator)world.getTileEntity(x, y, z));
			case REDSTONE:
				return new GuiRedstoneActivator(player.inventory, (TileEntityRedstoneActivator)world.getTileEntity(x, y, z));
			case RF:
				return new GuiRFActivator(player.inventory, (TileEntityRFActivator)world.getTileEntity(x, y, z));
		}
		return null;
	}

}

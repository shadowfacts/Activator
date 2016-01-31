package net.shadowfacts.activator.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.shadowfacts.activator.container.ContainerActivator;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.activator.tileentity.TileEntityRFActivator;
import net.shadowfacts.activator.tileentity.TileEntityRedstoneActivator;

/**
 * @author shadowfacts
 */
public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerActivator(player.inventory, (TileEntityActivator)world.getTileEntity(new BlockPos(x, y, z)));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (GUIs.values()[ID]) {
			case BASIC:
				return GUIActivator.create(player.inventory, (TileEntityActivator)world.getTileEntity(new BlockPos(x, y, z)));
			case REDSTONE:
				return GUIActivatorRedstone.create(player.inventory, (TileEntityRedstoneActivator)world.getTileEntity(new BlockPos(x, y, z)));
			case RF:
				return GUIActivatorRF.create(player.inventory, (TileEntityRFActivator)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

}

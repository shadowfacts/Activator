package net.shadowfacts.activator.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.network.PacketUpdateTE;

/**
 * @author shadowfacts
 */
public abstract class BaseTileEntity extends TileEntity {

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		save(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		load(tag);
	}

	public abstract NBTTagCompound save(NBTTagCompound tag);

	public abstract void load(NBTTagCompound tag);

	public void sync() {
		Side side = FMLCommonHandler.instance().getSide();
		if (side == Side.CLIENT) {
			Activator.network.sendToServer(new PacketUpdateTE(this));
		} else {
			Activator.network.sendToAllAround(new PacketUpdateTE(this), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 64));
		}
	}
}

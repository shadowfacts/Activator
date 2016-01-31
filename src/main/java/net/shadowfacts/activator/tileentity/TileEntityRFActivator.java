package net.shadowfacts.activator.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.activator.misc.RedstoneMode;

/**
 * @author shadowfacts
 */
public class TileEntityRFActivator extends TileEntityActivator implements IEnergyHandler {

	private static final String MODE = "Mode";

//	Non-persistent
	protected int prevRedstone;
	protected int redstone;

//	Persistent
	public EnergyStorage storage = new EnergyStorage(ActivatorConfig.rfCapacity);
	public RedstoneMode redstoneMode = RedstoneMode.HIGH;

	@Override
	public void updateEntity() {
		prevRedstone = redstone;
		redstone = worldObj.getBlockPowerInput(xCoord, yCoord, zCoord);

		super.updateEntity();
	}

	private boolean validateRedstone() {
		switch (redstoneMode) {
			case ALWAYS:
				return true;
			case NEVER:
				return false;
			case HIGH:
				return redstone != 0;
			case LOW:
				return redstone == 0;
			case PULSE:
				return redstone != prevRedstone;
			default:
				return false;
		}
	}

	@Override
	protected boolean canAttackEntity(Entity entity, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfAttackEnergy &&
				validateRedstone();
	}

	@Override
	protected boolean canBreakBlock(int x, int y, int z, Block block, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfBreakEnergy &&
				validateRedstone();
	}

	@Override
	protected boolean canRightClick(ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfRightClickEnergy &&
				validateRedstone();
	}

	@Override
	protected void postActivate(TileEntityActivator.Action action) {
		switch (action) {
			case ATTACK_ENTITY:
				storage.extractEnergy(ActivatorConfig.rfAttackEnergy, false);
				break;
			case BREAK_BLOCK:
				storage.extractEnergy(ActivatorConfig.rfBreakEnergy, false);
				break;
			case RIGHT_CLICK:
				storage.extractEnergy(ActivatorConfig.rfRightClickEnergy, false);
				break;
		}
	}

	//	Persistence
	@Override
	public NBTTagCompound save(NBTTagCompound tag) {
		tag = super.save(tag);

		storage.writeToNBT(tag);
		tag.setInteger(MODE, redstoneMode.ordinal());

		return tag;
	}

	@Override
	public void load(NBTTagCompound tag) {
		super.load(tag);

		storage.readFromNBT(tag);
		redstoneMode = RedstoneMode.get(tag.getInteger(MODE));
	}

//	IEnergyHandler
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (canConnectEnergy(from)) {
			int ret = storage.receiveEnergy(maxReceive, simulate);
			sync();
			return ret;
		}
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (canConnectEnergy(from)) {
			int ret = storage.extractEnergy(maxExtract, simulate);
			sync();
			return ret;
		}
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return from != getFacing();
	}
}

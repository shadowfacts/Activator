package net.shadowfacts.activator.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.shadowfacts.activator.ActivatorConfig;

/**
 * @author shadowfacts
 */
public class TileEntityRFActivator extends TileEntityActivator implements IEnergyHandler {

//	Persistent
	protected EnergyStorage storage = new EnergyStorage(ActivatorConfig.rfCapacity);

	@Override
	protected boolean canAttackEntity(Entity entity, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfAttackEnergy;
	}

	@Override
	protected boolean canBreakBlock(int x, int y, int z, Block block, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfBreakEnergy;
	}

	@Override
	protected boolean canRightClick(ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfRightClickEnergy;
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

		return tag;
	}

	@Override
	public void load(NBTTagCompound tag) {
		super.load(tag);

		storage.readFromNBT(tag);
	}

//	IEnergyHandler
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (canConnectEnergy(from)) {
			return storage.receiveEnergy(maxReceive, simulate);
		}
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (canConnectEnergy(from)) {
			return storage.extractEnergy(maxExtract, simulate);
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

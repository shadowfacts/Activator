package net.shadowfacts.activator.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.shadowfacts.activator.ActivatorConfig;
import net.shadowfacts.shadowmc.nbt.AutoSerializeNBT;

/**
 * @author shadowfacts
 */
public class TileEntityRFActivator extends TileEntityRedstoneActivator implements IEnergyHandler {

	private static final String MODE = "Mode";

	public EnergyStorage storage = new EnergyStorage(ActivatorConfig.rfCapacity);

	@Override
	protected boolean canAttackEntity(Entity entity, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfAttackEnergy &&
				canActivate;
	}

	@Override
	protected boolean canBreakBlock(BlockPos pos, IBlockState state, ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfBreakEnergy &&
				canActivate;
	}

	@Override
	protected boolean canRightClick(ItemStack stack) {
		return storage.getEnergyStored() >= ActivatorConfig.rfRightClickEnergy &&
				canActivate;
	}

	@Override
	protected void postActivate(Action action) {
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

	@Override
	public NBTTagCompound save(NBTTagCompound tag, boolean saveInventory) {
		tag = super.save(tag, saveInventory);
		storage.writeToNBT(tag);
		return tag;
	}

	@Override
	public void load(NBTTagCompound tag, boolean loadInventory) {
		super.load(tag, loadInventory);

		storage.readFromNBT(tag);
	}

//	IEnergyHandler
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (canConnectEnergy(from)) {
			int ret = storage.receiveEnergy(maxReceive, simulate);
			sync();
			return ret;
		}
		return 0;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		if (canConnectEnergy(from)) {
			int ret = storage.extractEnergy(maxExtract, simulate);
			sync();
			return ret;
		}
		return 0;
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return from != getFacing();
	}
}

package net.shadowfacts.activator.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.shadowfacts.activator.misc.RedstoneMode;

/**
 * @author shadowfacts
 */
public class TileEntityRedstoneActivator extends TileEntityActivator {

	private static final String MODE = "Mode";

//	Non-persistent
	protected int prevRedstone;
	protected int redstone;

//	Persistent
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
		return validateRedstone();
	}

	@Override
	protected boolean canBreakBlock(int x, int y, int z, Block block, ItemStack stack) {
		return validateRedstone();
	}

	@Override
	protected boolean canRightClick(ItemStack stack) {
		return validateRedstone();
	}

//	Persistence
	@Override
	public NBTTagCompound save(NBTTagCompound tag) {
		tag = super.save(tag);

		tag.setInteger(MODE, redstoneMode.ordinal());

		return tag;
	}

	@Override
	public void load(NBTTagCompound tag) {
		super.load(tag);
		redstoneMode = RedstoneMode.get(tag.getInteger(MODE));
	}

}

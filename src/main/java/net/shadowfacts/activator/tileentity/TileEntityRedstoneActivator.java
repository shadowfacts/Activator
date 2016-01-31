package net.shadowfacts.activator.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.shadowfacts.shadowmc.util.RedstoneMode;

/**
 * @author shadowfacts
 */
public class TileEntityRedstoneActivator extends TileEntityActivator {

	private static final String MODE = "Mode";

	protected boolean canActivate;

	protected int redstone;

	public RedstoneMode redstoneMode = RedstoneMode.HIGH;

	@Override
	public void update() {
		int redstone = worldObj.getRedstonePower(pos, getFacing());
		switch (redstoneMode) {
			case ALWAYS:
				canActivate = true;
				break;
			case NEVER:
				canActivate = false;
				break;
			case HIGH:
				canActivate = redstone != 0;
				break;
			case LOW:
				canActivate = redstone == 0;
				break;
			case PULSE:
				canActivate = this.redstone != redstone;
				break;

		}
		this.redstone = redstone;

		super.update();
	}

	@Override
	protected boolean canAttackEntity(Entity entity, ItemStack stack) {
		return canActivate;
	}

	@Override
	protected boolean canBreakBlock(BlockPos pos, IBlockState state, ItemStack stack) {
		return canActivate;
	}

	@Override
	protected boolean canRightClick(ItemStack stack) {
		return canActivate;
	}

	@Override
	protected void postActivate(Action action) {
		canActivate = false;
	}

	@Override
	public NBTTagCompound save(NBTTagCompound tag, boolean saveInventory) {
		tag = super.save(tag, saveInventory);
		tag.setInteger(MODE, redstoneMode.ordinal());
		return tag;
	}

	@Override
	public void load(NBTTagCompound tag, boolean loadInventory) {
		super.load(tag, loadInventory);
		redstoneMode = RedstoneMode.values()[tag.getInteger(MODE)];
	}

}

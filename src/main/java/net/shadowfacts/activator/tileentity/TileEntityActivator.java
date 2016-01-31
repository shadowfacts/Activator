package net.shadowfacts.activator.tileentity;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.shadowfacts.activator.Activator;
import net.shadowfacts.activator.block.BlockActivator;
import net.shadowfacts.activator.misc.ActivatorAction;
import net.shadowfacts.shadowmc.tileentity.BaseTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author shadowfacts
 */
public class TileEntityActivator extends BaseTileEntity implements IInventory, ITickable {

	private static final String INVENTORY = "Inventory";
	private static final String SLOT = "Slot";
	private static final String ACTIVATE_FREQUENCY = "ActivateFrequency";
	private static final String ACTION = "Action";
	private static final String SNEAKING = "Sneaking";
	public static final int MIN_UPDATE_FREQ = 20;
	public static final int MAX_UPDATE_FREQ = 1200;

	private static GameProfile profile = new GameProfile(UUID.fromString("52693A97-8EDB-5135-FE67-24B3600AEC87"), "[Activator]");

//	Persistent
	private ItemStack[] inventory = new ItemStack[1];
	public int activationFrequency = 20;
	@Getter
	private ActivatorAction action = ActivatorAction.RIGHT_CLICK;
	public boolean sneaking = false;

//	Non-persistent
	private int ticks = 0;
	private FakePlayer player;

	private boolean isBreaking;
	private int initialBlockDamage;
	private int curBlockDamage;
	private int durabilityRemaining;

	private List<Entity> detectedEntities = new ArrayList<>();

	public TileEntityActivator() {

	}

	protected Action activate() {
		FakePlayer player = getPlayer();

		EnumFacing facing = getFacing();

		double playerX = pos.getX();
		double playerY = pos.getY() - 1;
		double playerZ = pos.getZ();
		float yaw = 0;
		float pitch = 0;

		switch (facing) {
			case DOWN:
				playerY--;
				pitch = 90;
				break;
			case UP:
				playerY++;
				pitch = -90;
				break;
			case NORTH:
				playerZ--;
				yaw = 180;
				break;
			case SOUTH:
				playerZ++;
				yaw = 360;
				break;
			case WEST:
				playerX--;
				yaw = 270;
				break;
			case EAST:
				playerX++;
				yaw = 90;
				break;
		}

		player.setLocationAndAngles(playerX, playerY, playerZ, yaw, pitch);

		BlockPos target = getTargetLoc();

		ItemStack stack = getStackInSlot(0);

		IBlockState targetState = worldObj.getBlockState(target);

		Action result = Action.NOTHING;
		boolean done = false;

		if (action == ActivatorAction.LEFT_CLICK) {
			detectEntities(target);
			Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
			if (entity != null && canAttackEntity(entity, stack)) {
				if (stack != null) player.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers());
				player.attackTargetEntityWithCurrentItem(entity);
				result = Action.ATTACK_ENTITY;
				done = true;
			} else if (!isBreaking && canBreakBlock(target, targetState, stack)) {
				if (!targetState.getBlock().isAir(worldObj, target) &&
						targetState.getBlock().getBlockHardness(worldObj, target) >= 0) {
					isBreaking = true;
					startBreaking(targetState);
					result = Action.BREAK_BLOCK;
					done = true;
				}
			}
		} else if (action == ActivatorAction.RIGHT_CLICK && canRightClick(stack)) {

			EnumFacing side = facing.getOpposite();

			if (targetState.getBlock().isAir(worldObj, target)) {
				for (int i = 1; i < 5; i++) {
					BlockPos nextTarget = target.offset(facing, i);
					IBlockState state = worldObj.getBlockState(target);
					if (!state.getBlock().isAir(worldObj, target)) {
						targetState = state;
						target = nextTarget;
						break;
					}
				}
			}

			PlayerInteractEvent.Action action = targetState.getBlock().isAir(worldObj, target) ? PlayerInteractEvent.Action.RIGHT_CLICK_AIR : PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK;
			PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, action, worldObj, target, side);

			if (!event.isCanceled()) {
				result = Action.RIGHT_CLICK;

				Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
				done = entity != null && (entity instanceof EntityLiving && stack.getItem().itemInteractionForEntity(stack, player, (EntityLivingBase) entity) || (!(entity instanceof EntityAnimal) || ((EntityAnimal) entity).interact(player)));

				if (!done && stack != null) {
					stack.getItem().onItemUse(stack, player, worldObj, target, side, 0, 0, 0);
				}
				if (!done) {
					done = targetState.getBlock().onBlockActivated(worldObj, target, targetState, player, side, 0, 0, 0);
				}
				if (!done && stack != null) {
					done = stack.getItem().onItemUse(stack, player, worldObj, target, side, 0, 0, 0);
				}
				if (!done && stack != null) {
					stack = stack.getItem().onItemRightClick(stack, worldObj, player);
					done = true;
				}
			}
		}

		if (done) {
			if (stack == null || stack.stackSize == 0) {
				setInventorySlotContents(0, null);
			} else {
				setInventorySlotContents(0, stack);
			}
		}

		markDirty();

		return result;
	}

	/**
	 * Called to check if the activator can attack the entity
	 * Used to check any non-normal stuff, e.g. energy or redstone state
	 * @param entity The entity to attack
	 * @param stack The stack being attacked with
	 * @return If the entity can be attacked
	 */
	protected boolean canAttackEntity(Entity entity, ItemStack stack) {
		return true;
	}

	/**
	 * Called to check if the activator can break the block
	 * Used to check any non-normal stuff, e.g. energy or redstone state
	 * @param pos The coordinates of the block being broken
	 * @param state The state of the block being broken
	 * @param stack The stack the block is being broken with
	 * @return If the block can be broken
	 */
	protected boolean canBreakBlock(BlockPos pos, IBlockState state, ItemStack stack) {
		return true;
	}

	/**
	 * Called to check if the activator can right-click
	 * Used to check any non-normal stuff, e.g. energy or redstone state
	 * @param stack The stack to be right-clicked
	 * @return If the stack can be right click
	 */
	protected boolean canRightClick(ItemStack stack) {
		return true;
	}

	/**
	 * Called immediately after the activator actives
	 * @param action The action that was performed by the activator
	 */
	protected void postActivate(Action action) {

	}

	@Override
	public void update() {
		if (!worldObj.isRemote) {
			if (isBreaking) {
				BlockPos target = getTargetLoc();
				IBlockState targetState = worldObj.getBlockState(target);
				if (targetState.getBlock() != Blocks.air && !targetState.getBlock().isAir(worldObj, target)) {
					continueBreaking();
				}
			} else {
				if (ticks % activationFrequency == 0) {
					ticks = 0;
					postActivate(activate());
					sync();
				}
				ticks++;
			}
		}
	}

//	Persistence
	@Override
	public NBTTagCompound save(NBTTagCompound tag, boolean saveInventory) {
		NBTTagList invTagList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger(SLOT, i);
				inventory[i].writeToNBT(itemTag);
				invTagList.appendTag(itemTag);
			}
		}

		tag.setTag(INVENTORY, invTagList);
		tag.setInteger(ACTIVATE_FREQUENCY, activationFrequency);
		tag.setInteger(ACTION, action.ordinal());
		tag.setBoolean(SNEAKING, sneaking);
		return tag;
	}

	@Override
	public void load(NBTTagCompound tag, boolean loadInventory) {
		if (loadInventory) {
			NBTTagList invTagList = tag.getTagList(INVENTORY, 10);
			for (int i = 0; i < invTagList.tagCount(); i++) {
				NBTTagCompound itemTag = invTagList.getCompoundTagAt(i);
				int slot = itemTag.getInteger(SLOT);
				if (slot >= 0 && slot < getSizeInventory()) {
					inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
				}
			}
		}

		activationFrequency = tag.getInteger(ACTIVATE_FREQUENCY);
		action = ActivatorAction.values()[tag.getInteger(ACTION)];
		sneaking = tag.getBoolean(SNEAKING);
	}

//	Utilities
	protected EnumFacing getFacing() {
		return worldObj.getBlockState(pos).getValue(BlockActivator.FACING);
	}

	private FakePlayer getPlayer() {
		if (player == null) {
			player = FakePlayerFactory.get((WorldServer)worldObj, profile);
		}
		player.setCurrentItemOrArmor(0, getStackInSlot(0));
		return player;
	}

	private BlockPos getTargetLoc() {
		return getPos().offset(getFacing());
	}

	private void detectEntities(BlockPos pos) {
		double minX = pos.getX();
		double minY = pos.getY();
		double minZ = pos.getZ();
		double maxX = pos.getX() + 1;
		double maxY = pos.getY() + 1;
		double maxZ = pos.getZ() + 1;

		switch (getFacing()) {
			case DOWN:
				minY -= 5;
				break;
			case UP:
				maxY += 5;
				break;
			case NORTH:
				minZ -=5;
				break;
			case SOUTH:
				maxZ += 5;
				break;
			case WEST:
				minX -= 5;
				break;
			case EAST:
				maxX += 5;
		}

		AxisAlignedBB box = AxisAlignedBB.fromBounds(minX, minY, minZ, maxX, maxY, maxZ);
		detectedEntities = worldObj.getEntitiesWithinAABB(Entity.class, box);
	}

	public void increaseActivationFrequency(int amount) {
		activationFrequency = Math.min(activationFrequency + amount, MAX_UPDATE_FREQ);
	}

	public void decreaseActivationFrequency(int amount) {
		activationFrequency = Math.max(activationFrequency - amount, MIN_UPDATE_FREQ);
	}

	public void dropInventory() {
		ItemStack stack = getStackInSlot(0);
		if (stack != null && stack.stackSize > 0) {
			EntityItem item = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
			worldObj.spawnEntityInWorld(item);
		}
	}

//	Block breaking
	private void startBreaking(IBlockState state) {
		EnumFacing side = getFacing().getOpposite();
		BlockPos pos = getTargetLoc();

		PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, worldObj, pos, side);
		if (event.isCanceled()) {
			stopBreaking();
			return;
		}

		initialBlockDamage = curBlockDamage;
		float f = 1f;

		if (state.getBlock() != null) {
			if (event.useBlock != Event.Result.DENY) {
				f = state.getBlock().getPlayerRelativeBlockHardness(player, worldObj, pos);
			}
		}

		if (event.useItem == Event.Result.DENY) {
			stopBreaking();
			return;
		}

		if (f >= 1f) {
			tryHarvestBlock(pos);
			stopBreaking();
		} else {
			int remaining = (int)(f * 10);
			worldObj.sendBlockBreakProgress(player.getEntityId(), pos, remaining);
			durabilityRemaining = remaining;
		}
	}

	private void stopBreaking() {
		isBreaking = false;
		BlockPos pos = getTargetLoc();
		worldObj.sendBlockBreakProgress(player.getEntityId(), pos, -1);
		curBlockDamage = 0;
		initialBlockDamage = 0;
	}

	private void continueBreaking() {
		++curBlockDamage;
		BlockPos pos = getTargetLoc();

		IBlockState state = worldObj.getBlockState(pos);

		int i = curBlockDamage - initialBlockDamage;

		if (state.getBlock() == Blocks.air) {
			stopBreaking();
		} else {
			float remaining = blockType.getPlayerRelativeBlockHardness(player, worldObj, pos) * (float)(i + 1);
			int iRemaining = (int)(remaining * 10);
			if (iRemaining != durabilityRemaining) {
				worldObj.sendBlockBreakProgress(player.getEntityId(), pos, iRemaining);
				durabilityRemaining = iRemaining;
			}

			if (remaining >= 1f) {
				stopBreaking();
				tryHarvestBlock(pos);
			}
		}
	}

	private boolean tryHarvestBlock(BlockPos pos) {
		try {
			ItemStack stack = getStackInSlot(0);
			if (stack != null && stack.getItem().onBlockStartBreak(stack, pos, player)) {
				return false;
			}

			IBlockState state = worldObj.getBlockState(pos);

			boolean ret;
			boolean var1 = false;
			if (state.getBlock() != null) {
				var1 = state.getBlock().canHarvestBlock(worldObj, pos, player);
			}
			if (stack != null) {
				stack.getItem().onBlockDestroyed(stack, worldObj, state.getBlock(), pos, player);
			}
			ret = removeBlock(pos);
			if (ret && var1) {
				state.getBlock().harvestBlock(worldObj, player, pos, state, worldObj.getTileEntity(pos));
			}
			return ret;
		} catch (Exception e) {
			Activator.log.error("Problem harvesting block (" + e.toString() + ")");
			return false;
		}
	}

	private boolean removeBlock(BlockPos pos) {
		IBlockState state = worldObj.getBlockState(pos);
		if (state.getBlock() != null) {
			state.getBlock().onBlockHarvested(worldObj, pos, state, player);
		}
		boolean ret = false;
		if (state.getBlock() != null) {
			ret = state.getBlock().removedByPlayer(worldObj, pos, player, true);
			if (ret) {
				worldObj.playAuxSFXAtEntity(player, 2001, pos, Block.getIdFromBlock(state.getBlock()) + (state.getBlock().getMetaFromState(state) << 12));
				blockType.onBlockDestroyedByPlayer(worldObj, pos, state);
			}
		}
		return ret;
	}

	public void setAction(ActivatorAction action) {
		if (this.action == ActivatorAction.LEFT_CLICK && isBreaking) {
			stopBreaking();
		}
		this.action = action;
	}

	//	IInventory
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (inventory[slot] != null) {
			if (inventory[slot].stackSize <= amount) {
				ItemStack stack = inventory[slot];
				inventory[slot] = null;
				return stack;
			} else {
				ItemStack stack = inventory[slot].splitStack(amount);

				if (inventory[slot].stackSize == 0) {
					inventory[slot] = null;
				}

				return stack;

			}
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (inventory[slot] != null) {
			ItemStack stack = inventory[slot];
			inventory[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = null;
		}
	}

	@Override
	public String getName() {
		return "activator";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentTranslation(getName());
	}

//	Capabilities
	private IItemHandler itemHandler = new InvWrapper(this);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	public enum Action {
		ATTACK_ENTITY,
		BREAK_BLOCK,
		RIGHT_CLICK,
		NOTHING;
	}

}

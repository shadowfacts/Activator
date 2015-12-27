package net.shadowfacts.activator.tileentity;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.shadowfacts.activator.misc.ActivatorAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author shadowfacts
 */
public class TileEntityActivator extends BaseTileEntity implements IInventory {

	private static final String INVENTORY = "Inventory";
	private static final String SLOT = "Slot";
	private static final String ACTIVATE_FREQUENCY = "ActivateFrequency";
	private static final String ACTION = "Action";
	private static final String SNEAKING = "Sneaking";
	private static final int MIN_UPDATE_FREQ = 20;
	private static final int MAX_UPDATE_FREQ = 1200;

	private static GameProfile profile = new GameProfile(UUID.fromString("52693A97-8EDB-5135-FE67-24B3600AEC87"), "[Activator]");

//	Persistent
	private ItemStack[] inventory = new ItemStack[1];
	public int activateFrequency = 20;
	public ActivatorAction action = ActivatorAction.RIGHT_CLICK;
	public boolean sneaking = false;

//	Not persistent
	private int ticks = 0;
	private FakePlayer player;

	private boolean isBreaking = false;
	private int initialBlockDamage = 0;
	private int curBlockDamage = 0;
	private int durabilityRemainingOnBlock;

	private List<Entity> detectedEntities = new ArrayList<>();


//	Updating
	protected boolean activate() {
		FakePlayer player = getPlayer();

		ForgeDirection facing = getFacing();
		double playerX = xCoord;
		double playerY = yCoord - 1;
		double playerZ = zCoord;
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

		ChunkCoordinates target = getTargetLoc();

		ItemStack stack = getStackInSlot(0);

		Block targetBlock = worldObj.getBlock(target.posX, target.posY, target.posZ);

		boolean done = false;

		player.setSneaking(sneaking);

		if (action == ActivatorAction.LEFT_CLICK) {
			detectEntities(target);
			Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
			if (entity != null) { // attack entity
				if (stack != null) player.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers());
				player.attackTargetEntityWithCurrentItem(entity);
				done = true;
			} else if (!isBreaking) { // break block
				if (!targetBlock.isAir(worldObj, target.posX, target.posY, target.posZ) &&
						targetBlock.getBlockHardness(worldObj, target.posX, target.posY, target.posZ) >= 0) {
					isBreaking = true;
					startBreaking(targetBlock, worldObj.getBlockMetadata(target.posX, target.posY, target.posZ));
					done = true;
				}
			}
		} else if (action == ActivatorAction.RIGHT_CLICK) {

			ForgeDirection side = facing.getOpposite();

			if (targetBlock.isAir(worldObj, target.posX, target.posY, target.posZ)) {
				for (int i = 1; i < 5; i++) {
					int x = target.posX;
					int y = target.posY;
					int z = target.posZ;
					x += facing.offsetX * i;
					y += facing.offsetY * i;
					z += facing.offsetZ * i;
					Block block = worldObj.getBlock(x, y, z);
					if (!block.isAir(worldObj, x, y, z)) {
						targetBlock = block;
						target = new ChunkCoordinates(x, y, z);
						break;
					}
				}
			}


			PlayerInteractEvent.Action action = targetBlock.isAir(worldObj, target.posX, target.posY, target.posZ) ? PlayerInteractEvent.Action.RIGHT_CLICK_AIR : PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK;
			ForgeEventFactory.onPlayerInteract(player, action, target.posX, target.posY, target.posZ, side.ordinal(), worldObj);

			Entity entity = detectedEntities.isEmpty() ? null : detectedEntities.get(worldObj.rand.nextInt(detectedEntities.size()));
			done = entity != null && (entity instanceof EntityLiving && stack.getItem().itemInteractionForEntity(stack, player, (EntityLivingBase) entity) || (!(entity instanceof EntityAnimal) || ((EntityAnimal) entity).interact(player)));

			if (!done && stack != null) {
				stack.getItem().onItemUseFirst(stack, player, worldObj, target.posX, target.posY, target.posZ, side.ordinal(), 0, 0, 0);
			}
			if (!done) {
				done = targetBlock.onBlockActivated(worldObj, target.posX, target.posY, target.posZ, player, side.ordinal(), 0, 0, 0);
			}
			if (!done && stack != null) {
				done = stack.getItem().onItemUse(stack, player, worldObj, target.posX, target.posY, target.posZ, side.ordinal(), 0, 0, 0);
			}
			if (!done && stack != null) {
				stack = stack.getItem().onItemRightClick(stack, worldObj, player);
				done = true;
			}
		}

		if (done) {
			if (stack == null || stack.stackSize == 0) {
				setInventorySlotContents(0, null);
			} else {
				setInventorySlotContents(0, stack);
			}

			sync();
		}

		markDirty();

		return done;
	}

	protected void postActivate(boolean success) {

	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote) {
			if (isBreaking) {
				ChunkCoordinates coords = getTargetLoc();
				Block block = worldObj.getBlock(coords.posX, coords.posY, coords.posZ);
				if (block != Blocks.air && !block.isAir(worldObj, coords.posX, coords.posY, coords.posZ)) {
					continueBreaking();
				}
			} else {
				if (ticks % activateFrequency == 0) {
					ticks = 0;
					postActivate(activate());
				}
				ticks++;
			}
		}
	}

	//	Persistence
	@Override
	public NBTTagCompound save(NBTTagCompound tag) {
		NBTTagList inventoryTagList = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				NBTTagCompound savedStack = new NBTTagCompound();
				savedStack.setInteger(SLOT, i);
				inventory[i].writeToNBT(savedStack);
				inventoryTagList.appendTag(savedStack);
			}
		}

		tag.setTag(INVENTORY, inventoryTagList);
		tag.setInteger(ACTIVATE_FREQUENCY, activateFrequency);
		tag.setInteger(ACTION, action.ordinal());
		tag.setBoolean(SNEAKING, sneaking);
		return tag;
	}

	@Override
	public void load(NBTTagCompound tag) {
		NBTTagList inventoryTagList = tag.getTagList(INVENTORY, 10);
		for (int i = 0; i < inventoryTagList.tagCount(); ++i) {
			NBTTagCompound savedStack = inventoryTagList.getCompoundTagAt(i);
			int slot = savedStack.getInteger(SLOT);
			if (slot >= 0 && slot < getSizeInventory()) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(savedStack);
			}
		}
		activateFrequency = tag.getInteger(ACTIVATE_FREQUENCY);
		action = ActivatorAction.get(tag.getInteger(ACTION));
		sneaking = tag.getBoolean(SNEAKING);
	}

	//	Utilities
	private ForgeDirection getFacing() {
		return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}

	private FakePlayer getPlayer() {
		if (player == null) {
			player = FakePlayerFactory.get((WorldServer)worldObj, profile);
		}
		player.setCurrentItemOrArmor(0, getStackInSlot(0));
		return player;
	}

	private ChunkCoordinates getTargetLoc() {
		ChunkCoordinates coords = new ChunkCoordinates(xCoord, yCoord, zCoord);
		ForgeDirection facing = getFacing();
		coords.posX += facing.offsetX;
		coords.posY += facing.offsetY;
		coords.posZ += facing.offsetZ;
		return coords;
	}

	@SuppressWarnings("unchecked")
	private void detectEntities(ChunkCoordinates coords) {
		double minX = coords.posX;
		double minY = coords.posY;
		double minZ = coords.posZ;
		double maxX = coords.posX + 1;
		double maxY = coords.posY + 1;
		double maxZ = coords.posZ + 1;

		switch (getFacing()) {
			case DOWN:
				minY -= 5;
				break;
			case UP:
				maxY += 5;
				break;
			case NORTH:
				minZ -= 5;
				break;
			case SOUTH:
				maxZ += 5;
				break;
			case WEST:
				minX -= 5;
				break;
			case EAST:
				maxX += 5;
				break;
		}

		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		detectedEntities = worldObj.getEntitiesWithinAABB(Entity.class, box);
	}

	public void increaseActivateFrequency(int amount) {
		activateFrequency = Math.min(activateFrequency + amount, MAX_UPDATE_FREQ);
	}

	public void decreaseActivateFrequency(int amount) {
		activateFrequency = Math.max(activateFrequency - amount, MIN_UPDATE_FREQ);
	}

	public void dropInventory() {
		ItemStack stack = getStackInSlot(0);
		if (stack != null && stack.stackSize > 0) {
			EntityItem entityItem = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
			worldObj.spawnEntityInWorld(entityItem);
		}
	}

//	Block breaking
	private void startBreaking(Block block, int meta) {
		ForgeDirection side = getFacing().getOpposite();
		ChunkCoordinates coords = getTargetLoc();

		PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, coords.posX, coords.posY, coords.posZ, side.ordinal(), worldObj);
		if (event.isCanceled()) {
			stopBreaking();
			return;
		}

		initialBlockDamage = curBlockDamage;
		float f = 1f;

		if (block != null) {
			if (event.useBlock != Event.Result.DENY) {
				f = block.getPlayerRelativeBlockHardness(player, worldObj, coords.posX, coords.posY, coords.posZ);
			}
		}

		if (event.useItem == Event.Result.DENY) {
			stopBreaking();
			return;
		}

		if (f >= 1f) {
			tryHarvestBlock(coords.posX, coords.posY, coords.posZ);
			stopBreaking();
		} else {
			int remaining = (int)(f * 10);
			worldObj.destroyBlockInWorldPartially(player.getEntityId(), coords.posX, coords.posY, coords.posZ, remaining);
			durabilityRemainingOnBlock = remaining;
		}

	}

	private void stopBreaking() {
		isBreaking = false;
		ChunkCoordinates coords = getTargetLoc();
		worldObj.destroyBlockInWorldPartially(player.getEntityId(), coords.posX, coords.posY, coords.posZ, -1);
		curBlockDamage = 0;
		initialBlockDamage = 0;
	}

	private void continueBreaking() {
		++curBlockDamage;
		ChunkCoordinates coords = getTargetLoc();

		Block block = worldObj.getBlock(coords.posX, coords.posY, coords.posZ);

		int i = curBlockDamage - initialBlockDamage;

		if (block == Blocks.air) {
			stopBreaking();
		} else {
			float remaining = block.getPlayerRelativeBlockHardness(player, worldObj, coords.posX, coords.posY, coords.posZ) * (float)(i + 1);
			int left = (int)(remaining * 10);
			if (left != durabilityRemainingOnBlock) {
				worldObj.destroyBlockInWorldPartially(player.getEntityId(), coords.posX, coords.posY, coords.posZ, left);
				durabilityRemainingOnBlock = left;
			}

			if (remaining >= 1f) {
				stopBreaking();
				tryHarvestBlock(coords.posX, coords.posY, coords.posZ);
			}
		}
	}

	private boolean tryHarvestBlock(int x, int y, int z) {
		ItemStack stack = getStackInSlot(0);
		if (stack != null && stack.getItem().onBlockStartBreak(stack, x, y, z, player)) {
			return false;
		}

		Block block = worldObj.getBlock(x, y, z);
		int meta = worldObj.getBlockMetadata(x, y, z);

		boolean ret;
		boolean var1 = false;
		if (block != null) {
			var1 = block.canHarvestBlock(player, meta);
		}
		if (stack != null) {
			stack.getItem().onBlockDestroyed(stack, worldObj, block, x, y, z, player);
		}
		ret = removeBlock(x, y, z);
		if (ret && var1) {
			block.harvestBlock(worldObj, player, x, y, z, meta);
		}
		return ret;
	}

	private boolean removeBlock(int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		int meta = worldObj.getBlockMetadata(x, y, z);

		if (block != null) {
			block.onBlockHarvested(worldObj, x, y, z, meta, player);
		}

		boolean ret = false;
		if (block != null) {
			ret = block.removedByPlayer(worldObj, player, x, y, z, true);
			if (ret) {
				worldObj.playAuxSFXAtEntity(player, 2001, x, y, z, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(x, y, z) << 12));
				block.onBlockDestroyedByPlayer(worldObj, x, y, z, meta);
			}
		}

		return ret;
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
	public ItemStack getStackInSlotOnClosing(int slot) {
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
	public String getInventoryName() {
		return "activator";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + .5d, yCoord + .5d, zCoord + .5d) <= 64d;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
}

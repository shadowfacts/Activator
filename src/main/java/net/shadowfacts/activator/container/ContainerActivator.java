package net.shadowfacts.activator.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.shadowfacts.activator.tileentity.TileEntityActivator;
import net.shadowfacts.shadowmc.inventory.ContainerBase;

/**
 * @author shadowfacts
 */
public class ContainerActivator extends ContainerBase {

	private TileEntityActivator activator;

	public ContainerActivator(InventoryPlayer playerInv, TileEntityActivator activator) {
		super(activator.getPos());
		this.activator = activator;

		addSlotToContainer(new Slot(activator, 0, 80, 17));

//		Player inv
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (slotNum < 1) {
				if (!mergeItemStack(itemStack1, 1, 37, false)) {
					return null;
				}
			} else if (!mergeItemStack(itemStack1, 0, 1, false)) {
				return null;
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemStack1);

		}

		return itemStack;
	}

}

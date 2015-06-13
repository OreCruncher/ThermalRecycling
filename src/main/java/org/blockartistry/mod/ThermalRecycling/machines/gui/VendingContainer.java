/*
 * This file is part of ThermalRecycling, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.mod.ThermalRecycling.machines.gui;

import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cofh.lib.gui.slot.SlotLocked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * A good chunk of this code was sourced from the following:
 * 
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-containers.html
 */
public final class VendingContainer extends MachineContainer<VendingTileEntity> {

	public VendingContainer(final InventoryPlayer inv,
			final IInventory tileEntity) {
		super((VendingTileEntity) tileEntity);

		// GUI dimension is width 427, height 240
		final IInventory inventory = entity.getMachineInventory();

		// Add configuration slots
		for (int i = 0; i < 6; i++) {

			final int slotBase = VendingTileEntity.CONFIG_SLOT_START + i;
			final int x = (i < 3) ? 17 : 97;
			final int y = (i < 3) ? i * GUI_INVENTORY_CELL_SIZE + 17 : (i - 3)
					* GUI_INVENTORY_CELL_SIZE + 17;

			Slot slot = new SlotLocked(inventory, slotBase, x, y);
			addSlotToContainer(slot);

			slot = new SlotLocked(inventory, slotBase + 6, x
					+ GUI_INVENTORY_CELL_SIZE, y);
			addSlotToContainer(slot);

			final MultiSlot ms = new MultiSlot(inventory, slotBase + 12, x
					+ GUI_INVENTORY_CELL_SIZE * 2 + 9, y);
			ms.setInfinite().setPhantom();
			addSlotToContainer(ms);
		}

		addPlayerInventory(inv, 166);
	}

	@Override
	public ItemStack slotClick(final int slotIndex, final int button, final int modifier,
			final EntityPlayer player) {

		if (player == null) {
			return null;
		}

		final Slot slot = slotIndex < 0 ? null : (Slot) this.inventorySlots
				.get(slotIndex);
		if (slot instanceof MultiSlot) {
			if (((MultiSlot) slot).isPhantom()) {
				return doTrade(slot, player);
			}
		}

		return super.slotClick(slotIndex, button, modifier, player);
	}

	protected ItemStack doTrade(final Slot slot, final EntityPlayer player) {

		final InventoryPlayer playerInventory = player.inventory;

		// Get the result of the potential trade. If there is nothing,
		// or the player inventory cannot hold the stack then return null.
		final ItemStack result = slot.getStack();
		if (result == null
				|| !canPlayerAccept(playerInventory, result, result.stackSize))
			return null;

		// Can the inventory provide the result item?
		if (!doesInventoryContain(result, result.stackSize,
				VendingTileEntity.INVENTORY_SLOT_START,
				VendingTileEntity.GENERAL_INVENTORY_SIZE - 1))
			return null;

		// Get the input slots. We do some math on the slot index to
		// figure out the right input slots.
		final int index = slot.slotNumber / 3
				+ VendingTileEntity.CONFIG_SLOT_START;
		final ItemStack input1 = entity.getStackInSlot(index);
		final ItemStack input2 = entity.getStackInSlot(index + 6);

		// See if the vending inventory can accept the required items
		if (!canInventoryAccept(input1, input2,
				VendingTileEntity.INVENTORY_SLOT_START,
				VendingTileEntity.GENERAL_INVENTORY_SIZE - 1))
			return null;

		// See if the player can provide the payment
		if (!canPlayerInventoryProvide(playerInventory, input1, input2))
			return null;

		// OK - things should work. Do the transaction. Handle Vending
		// Machine first.
		if (input1 != null) {
			if (!entity.isAdminMode()) {
				entity.addStackToOutput(input1.copy());
			}
			ItemStackHelper.removeItemStackFromInventory(
					playerInventory.mainInventory, input1.copy(), 0, 36);
		}
		if (input2 != null) {
			if (!entity.isAdminMode()) {
				entity.addStackToOutput(input2.copy());
			}
			ItemStackHelper.removeItemStackFromInventory(
					playerInventory.mainInventory, input2.copy(), 0, 36);
		}

		if (!entity.isAdminMode()) {
			entity.removeStackFromOutput(result.copy());
		}

		playerInventory.addItemStackToInventory(result.copy());

		player.onUpdate();

		return null;
	}

	protected boolean canPlayerInventoryProvide(
			final InventoryPlayer playerInventory, final ItemStack stack1,
			final ItemStack stack2) {

		// If no payment is required then sure, the player
		// can provide nothing.
		if (stack1 == null && stack2 == null)
			return true;

		if (ItemStackHelper.areEqual(stack1, stack2)) {
			int count = stack1.stackSize + stack2.stackSize;
			for (int i = 0; i < playerInventory.getSizeInventory() && count > 0; i++) {
				final ItemStack inv = playerInventory.getStackInSlot(i);
				if (ItemStackHelper.areEqual(stack1, inv)) {
					count -= inv.stackSize;
				}
			}

			return count < 1;

		} else {

			// Two different types of stacks. Have to handle nulls.
			int count1 = stack1 != null ? stack1.stackSize : 0;
			int count2 = stack2 != null ? stack2.stackSize : 0;
			for (int i = 0; i < playerInventory.getSizeInventory()
					&& (count1 > 0 || count2 > 0); i++) {
				final ItemStack inv = playerInventory.getStackInSlot(i);
				if (inv == null)
					continue;

				if (ItemStackHelper.areEqual(inv, stack1)) {
					count1 -= inv.stackSize;
				} else if (ItemStackHelper.areEqual(inv, stack2)) {
					count2 -= inv.stackSize;
				}
			}

			return count1 < 1 && count2 < 1;
		}
	}

	protected boolean canInventoryAccept(final ItemStack stack1,
			final ItemStack stack2, final int slotStart, final int slotEnd) {

		// Admin Vending Machines can always accept payment
		if (entity.isAdminMode())
			return true;

		// If both are null then payment is not required
		if (stack1 == null && stack2 == null)
			return true;

		if (ItemStackHelper.areEqual(stack1, stack2)) {
			int count = stack1.stackSize + stack2.stackSize;
			for (int i = slotStart; i <= slotEnd && count > 0; i++) {
				final ItemStack inv = entity.getStackInSlot(i);
				if (inv == null) {
					count -= stack1.getMaxStackSize();
				} else if (ItemStackHelper.areEqual(stack1, inv)) {
					count -= inv.getMaxStackSize() - inv.stackSize;
				}
			}

			return count < 1;

		} else {

			// Two different types of stacks. Have to handle nulls.
			int count1 = stack1 != null ? stack1.stackSize : 0;
			int count2 = stack2 != null ? stack2.stackSize : 0;
			for (int i = slotStart; i <= slotEnd && (count1 > 0 || count2 > 0); i++) {
				final ItemStack inv = entity.getStackInSlot(i);
				if (inv == null) {
					if (count1 > 0) {
						count1 -= stack1.getMaxStackSize();
					} else if (count2 > 0) {
						count2 -= stack2.getMaxStackSize();
					}
				} else if (ItemStackHelper.areEqual(inv, stack1)) {
					count1 -= inv.getMaxStackSize() - inv.stackSize;
				} else if (ItemStackHelper.areEqual(inv, stack2)) {
					count2 -= inv.getMaxStackSize() - inv.stackSize;
				}
			}

			return count1 < 1 && count2 < 1;
		}
	}

	// Determines if the vending inventory contains enough of the
	// items to give to the player.
	protected boolean doesInventoryContain(final ItemStack stack,
			final int amount, final int slotStart, final int slotEnd) {

		// If it is an Admin Vending Machine it can always satisfy a request.
		if (entity.isAdminMode())
			return true;

		int count = amount;
		for (int i = slotStart; i <= slotEnd && count > 0; i++) {
			final ItemStack inv = entity.getStackInSlot(i);
			if (inv != null && ItemStackHelper.areEqual(inv, stack)) {
				count -= inv.stackSize;
			}
		}

		return count < 1;
	}

	// Determines if the player has space in his inventory to hold
	// the ItemStack.
	protected boolean canPlayerAccept(final InventoryPlayer inventoryPlayer,
			final ItemStack stack, final int amount) {

		// Loop through the inventory decrementing the count until it hits
		// zero or less. If > 0 at the end of the loop there is no space.
		int count = amount;
		for (int i = 0; i < inventoryPlayer.getSizeInventory() && count > 0; i++) {
			final ItemStack inv = inventoryPlayer.getStackInSlot(i);
			if (inv == null) {
				count -= stack.getMaxStackSize();
			} else if (ItemStackHelper.areEqual(stack, inv)) {
				count -= inv.getMaxStackSize() - inv.stackSize;
			}
		}

		return count < 1;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn,
			final int slotIndex) {
		// No shift+click behavior in this GUI
		return null;
	}
}

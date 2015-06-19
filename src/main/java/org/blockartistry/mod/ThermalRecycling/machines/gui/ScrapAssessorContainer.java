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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ScrapAssessorTileEntity;
import cofh.lib.gui.slot.SlotAcceptValid;
import cofh.lib.gui.slot.SlotLocked;

public final class ScrapAssessorContainer extends
		MachineContainer<ScrapAssessorTileEntity> {
	
	private MachineStatus currentStatus = MachineStatus.IDLE;

	public ScrapAssessorContainer(final InventoryPlayer inv,
			final IInventory tileEntity) {
		super((ScrapAssessorTileEntity) tileEntity);

		final IInventory inventory = entity.getMachineInventory();
		Slot s = new SlotAcceptValid(inventory, ScrapAssessorTileEntity.INPUT,
				11, 13);
		addSlotToContainer(s);

		s = new SlotAcceptValid(inventory, ScrapAssessorTileEntity.CORE, 33, 34);
		addSlotToContainer(s);

		s = new SlotLocked(inventory, ScrapAssessorTileEntity.SAMPLE, 56, 34);
		addSlotToContainer(s);

		for (int i = 0; i < ScrapAssessorTileEntity.DISPLAY_SLOTS.length; i++) {

			final int oSlot = ScrapAssessorTileEntity.DISPLAY_SLOTS[i];

			final int h = (i % 3) * GUI_INVENTORY_CELL_SIZE + 106;
			final int v = (i / 3) * GUI_INVENTORY_CELL_SIZE + 17;

			s = new SlotLocked(inventory, oSlot, h, v);
			addSlotToContainer(s);
		}

		addPlayerInventory(inv, 166);
	}

	@Override
	public void handleStatus() {

		final MachineStatus status = entity.getStatus();

		for (int i = 0; i < crafters.size(); ++i) {

			final ICrafting icrafting = (ICrafting) crafters.get(i);

			if (status != currentStatus)
				icrafting.sendProgressBarUpdate(this,
						ScrapAssessorTileEntity.UPDATE_ACTION_STATUS,
						status.ordinal());
		}

		currentStatus = status;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn,
			final int slotIndex) {

		ItemStack stack = null;
		final Slot slot = (Slot) inventorySlots.get(slotIndex);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slot != null && slot.getHasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			// If the slot is INPUT or one of the OUTPUTs, move the contents
			// to the player inventory
			if (slotIndex < 11) {

				if (!mergeToPlayerInventory(stackInSlot)) {
					return null;
				}

			} else {

				final int targetSlot = CoreType.isProcessingCore(stackInSlot) ? ScrapAssessorTileEntity.CORE
						: ScrapAssessorTileEntity.INPUT;
				if (entity.isItemValidForSlot(targetSlot, stackInSlot)) {
					// Try moving to the input slot
					if (!mergeItemStack(stackInSlot, targetSlot, targetSlot + 1, false)) {
						return null;
					}
				}
			}

			// Cleanup the stack
			if (stackInSlot.stackSize == 0) {
				slot.putStack(null);
			}

			// Nothing changed
			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
		}

		return stack;
	}
}

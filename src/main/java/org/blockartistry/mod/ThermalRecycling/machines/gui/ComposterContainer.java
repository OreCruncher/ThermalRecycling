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

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ComposterTileEntity;
import cofh.lib.gui.slot.SlotAcceptValid;
import cofh.lib.gui.slot.SlotLocked;

public final class ComposterContainer extends MachineContainer<ComposterTileEntity> {

	private MachineStatus currentStatus = MachineStatus.IDLE;
	private int currentProgress = 0;
	private int currentWater = 0;

	public ComposterContainer(final InventoryPlayer inv, final IInventory tileEntity) {
		super((ComposterTileEntity)tileEntity);

		Slot s = new SlotAcceptValid(entity, ComposterTileEntity.BROWN, 42, 32);
		addSlotToContainer(s);

		s = new SlotAcceptValid(entity, ComposterTileEntity.GREEN1, 63, 32);
		addSlotToContainer(s);

		s = new SlotAcceptValid(entity, ComposterTileEntity.GREEN2, 84, 32);
		addSlotToContainer(s);

		s = new SlotLocked(entity, ComposterTileEntity.MEAL, 134, 32);
		addSlotToContainer(s);

		addPlayerInventory(inv);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void handleStatus() {

		final MachineStatus status = entity.getStatus();
		final int progress = entity.getProgress();
		final int water = entity.getFluidTank().getFluidAmount();

		for (int i = 0; i < crafters.size(); ++i) {

			final ICrafting icrafting = (ICrafting) crafters.get(i);

			if (progress != currentProgress)
				icrafting.sendProgressBarUpdate(this,
						ComposterTileEntity.UPDATE_ACTION_PROGRESS, progress);
			if (water != currentWater)
				icrafting.sendProgressBarUpdate(this,
						ComposterTileEntity.UPDATE_WATER_LEVEL, water);
			if (status != currentStatus)
				icrafting.sendProgressBarUpdate(this,
						ComposterTileEntity.UPDATE_ACTION_STATUS,
						status.ordinal());
		}

		currentStatus = status;
		currentProgress = progress;
		currentWater = water;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int slotIndex) {

		ItemStack stack = null;
		final Slot slot = (Slot) inventorySlots.get(slotIndex);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slot != null && slot.getHasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			// Trying to move stuff out of the machine inventory
			if (slotIndex < 3) {

				if (!mergeToPlayerInventory(stackInSlot)) {
					return null;
				}

			} else {

				// Attempt to move stuff into machine inventory. If it is
				// not a compost ingredient return null.
				final ItemData data = ItemData.get(stackInSlot);
				if (data == null
						|| data.getCompostIngredientValue() == CompostIngredient.NONE)
					return null;

				// Based on the type of ingredient determines which slot we
				// try to move it into
				boolean mergeResult = false;
				if (data.getCompostIngredientValue() == CompostIngredient.BROWN) {
					mergeResult = mergeItemStack(stackInSlot,
							ComposterTileEntity.BROWN, 1, false);
				} else {
					mergeResult = mergeItemStack(stackInSlot,
							ComposterTileEntity.GREEN1, 3, false);
				}

				if (!mergeResult)
					return null;
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

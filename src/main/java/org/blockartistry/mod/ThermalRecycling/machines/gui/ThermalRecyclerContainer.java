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

import org.blockartistry.mod.ThermalRecycling.machines.entity.ThermalRecyclerTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import cofh.lib.gui.slot.SlotAcceptValid;
import cofh.lib.gui.slot.SlotRemoveOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * A good chunk of this code was sourced from the following:
 * 
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-containers.html
 */
public final class ThermalRecyclerContainer extends MachineContainer<ThermalRecyclerTileEntity> {

	MachineStatus currentStatus;
	int currentProgress;
	int currentEnergy;
	int currentEnergyRate;
	
	public ThermalRecyclerContainer(final InventoryPlayer inv, final IInventory tileEntity) {
		super((ThermalRecyclerTileEntity)tileEntity);

		// GUI dimension is width 427, height 240
		currentStatus = MachineStatus.IDLE;
		currentProgress = 0;
		currentEnergy = 0;
		currentEnergyRate = 0;
		spamCycle = 0;

		Slot s = new SlotAcceptValid(entity, ThermalRecyclerTileEntity.INPUT,
				56, 34);
		addSlotToContainer(s);

		for (int i = 0; i < ThermalRecyclerTileEntity.OUTPUT_SLOTS.length; i++) {

			final int oSlot = ThermalRecyclerTileEntity.OUTPUT_SLOTS[i];

			final int h = (i % 3) * 18 + 106;
			final int v = (i / 3) * 18 + 17;

			s = new SlotRemoveOnly(entity, oSlot, h, v);
			addSlotToContainer(s);
		}

		s = new SlotAcceptValid(entity, ThermalRecyclerTileEntity.CORE, 33, 34);
		addSlotToContainer(s);
		
		addPlayerInventory(inv);
	}

	@Override
	public void handleStatus() {

		final MachineStatus status = entity.getStatus();
		final int progress = entity.getProgress();
		final int energy = entity.getInfoEnergyStored();
		final int energyRate = entity.getInfoEnergyPerTick();

		for (int i = 0; i < crafters.size(); ++i) {

			final ICrafting icrafting = (ICrafting) crafters.get(i);

			if (progress != currentProgress)
				icrafting.sendProgressBarUpdate(this,
						ThermalRecyclerTileEntity.UPDATE_ACTION_PROGRESS,
						progress);
			if (energy != currentEnergy)
				icrafting.sendProgressBarUpdate(this,
						ThermalRecyclerTileEntity.UPDATE_ACTION_ENERGY, energy/10);
			if (energyRate != currentEnergyRate)
				icrafting.sendProgressBarUpdate(this,
						ThermalRecyclerTileEntity.UPDATE_ACTION_ENERGY_RATE,
						energyRate);
			if (status != currentStatus)
				icrafting.sendProgressBarUpdate(this,
						ThermalRecyclerTileEntity.UPDATE_ACTION_STATUS,
						status.ordinal());
		}

		currentStatus = status;
		currentProgress = progress;
		currentEnergy = energy;
		currentEnergyRate = energyRate;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int slotIndex) {

		ItemStack stack = null;
		final Slot slot = (Slot) inventorySlots.get(slotIndex);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slot != null && slot.getHasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			// If the slot is INPUT or one of the OUTPUTs, move the contents
			// to the player inventory
			if (MyUtils.contains(ThermalRecyclerTileEntity.ALL_SLOTS, slotIndex)) {

				if (!mergeItemStack(stackInSlot, sizeInventory,
						sizeInventory + 36, false)) {
					return null;
				}

				slot.onSlotChange(stackInSlot, stack);

			} else if (entity.isItemValidForSlot(
					ThermalRecyclerTileEntity.INPUT, stackInSlot)) {

				// Try moving to the input slot
				if (!mergeItemStack(stackInSlot, 0, 1, false)) {
					return null;
				}

				slot.onSlotChange(stackInSlot, stack);
			}

			// Cleanup the stack
			if (stackInSlot.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			// Nothing changed
			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, stackInSlot);
		}

		return stack;
	}
}

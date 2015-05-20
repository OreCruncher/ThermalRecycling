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
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ComposterTileEntity;
import cofh.lib.gui.slot.SlotAcceptValid;
import cofh.lib.gui.slot.SlotLocked;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ComposterContainer extends Container {

	ComposterTileEntity entity;
	int sizeInventory;

	MachineStatus currentStatus = MachineStatus.IDLE;
	int currentProgress;
	int currentWater;
	
	int spamCycle;

	public ComposterContainer(InventoryPlayer inv, IInventory tileEntity) {

		entity = (ComposterTileEntity) tileEntity;
		sizeInventory = entity.getSizeInventory();

		Slot s = new SlotAcceptValid(entity, ComposterTileEntity.BROWN, 42, 32);
		addSlotToContainer(s);

		s = new SlotAcceptValid(entity, ComposterTileEntity.GREEN1, 63, 32);
		addSlotToContainer(s);

		s = new SlotAcceptValid(entity, ComposterTileEntity.GREEN2, 84, 32);
		addSlotToContainer(s);

		s = new SlotLocked(entity, ComposterTileEntity.MEAL, 134, 32);
		addSlotToContainer(s);

		// Add the player inventory
		for (int i = 0; i < 27; ++i) {

			// The constants are offsets from the left and top edge
			// of the GUI
			int h = (i % 9) * 18 + 8;
			int v = (i / 9) * 18 + 84;

			// We offset by 9 to skip the hotbar slots - they
			// come next
			s = new Slot(inv, i + 9, h, v);
			addSlotToContainer(s);
		}

		// Add the hotbar
		for (int i = 0; i < 9; ++i) {
			s = new Slot(inv, i, 8 + i * 18, 142);
			addSlotToContainer(s);
		}
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {

		super.detectAndSendChanges();
		
		if((++spamCycle % 3) != 0) {
			return;
		}

		MachineStatus status = entity.getStatus();
		int progress = entity.getProgress();
		int water = entity.getFluidTank().getFluidAmount();

		for (int i = 0; i < crafters.size(); ++i) {

			ICrafting icrafting = (ICrafting) crafters.get(i);

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
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		entity.receiveClientEvent(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return entity.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {

		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slot != null && slot.getHasStack()) {

			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			// Trying to move stuff out of the machine inventory
			if (slotIndex < 3) {

				if (!mergeItemStack(stackInSlot, sizeInventory,
						sizeInventory + 36, false)) {
					return null;
				}
				slot.onSlotChange(stackInSlot, stack);

			} else {

				// Attempt to move stuff into machine inventory. If it is
				// not a compost ingredient return null.
				ItemScrapData data = ItemScrapData.get(stackInSlot);
				if (data == null
						|| data.getCompostIngredientValue() == CompostIngredient.NONE)
					return null;

				// Based on the type of ingredient determines which slot we
				// try to move it into
				boolean mergeResult = false;
				if (data.getCompostIngredientValue() == CompostIngredient.BROWN)
					mergeResult = mergeItemStack(stackInSlot,
							ComposterTileEntity.BROWN, 1, false);
				else
					mergeResult = mergeItemStack(stackInSlot,
							ComposterTileEntity.GREEN1, 2, false);

				if (!mergeResult)
					return null;

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

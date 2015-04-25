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

import cofh.lib.gui.slot.SlotRemoveOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * A good chunk of this code was sourced from the following:
 * 
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-containers.html
 */
public class ThermalRecyclerContainer extends Container {

	ThermalRecyclerTileEntity entity;
	int sizeInventory;
	int currentProgress;

	protected static boolean contains(int[] list, int value) {
		for (int i : list)
			if (i == value)
				return true;
		return false;
	}

	public ThermalRecyclerContainer(InventoryPlayer inv, IInventory tileEntity) {

		currentProgress = 0;
		entity = (ThermalRecyclerTileEntity) tileEntity;
		sizeInventory = entity.getSizeInventory();

		Slot s = new Slot(entity, ThermalRecyclerTileEntity.INPUT, 56, 35);
		addSlotToContainer(s);

		for (int oSlot : ThermalRecyclerTileEntity.OUTPUT_SLOTS) {
			s = new SlotRemoveOnly(entity, oSlot, 116 + (oSlot - 1) * 18, 35);
			addSlotToContainer(s);
		}

		// add player inventory slots
		// note that the slot numbers are within the player inventory so can
		// be same as the tile entity inventory
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				s = new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18);
				addSlotToContainer(s);
			}
		}

		// add hotbar slots
		for (i = 0; i < 9; ++i) {
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
		if (entity.getProgress() != currentProgress) {
			// send an update
			currentProgress = entity.getProgress();
			for (int j = 0; j < this.crafters.size(); ++j) {
				// We fake out this process based on percentage progress. Each
				// item
				// takes 100% to process
				((ICrafting) this.crafters.get(j)).sendProgressBarUpdate(this,
						2, currentProgress);
				((ICrafting) this.crafters.get(j)).sendProgressBarUpdate(this,
						0, 100);
				((ICrafting) this.crafters.get(j)).sendProgressBarUpdate(this,
						1, 100);
			}
		}
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT) public void updateProgressBar(int id, int data) {
	 * entity.setField(id, data); }
	 */

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return entity.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
		ItemStack itemStack1 = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack1 = itemStack2.copy();

			if (contains(ThermalRecyclerTileEntity.OUTPUT_SLOTS, slotIndex)) {
				if (!mergeItemStack(itemStack2, sizeInventory,
						sizeInventory + 36, true)) {
					return null;
				}

				slot.onSlotChange(itemStack2, itemStack1);
			} else if (slotIndex != ThermalRecyclerTileEntity.INPUT) {
				return null;
				/*
				 * // check if there is a grinding recipe for the stack if
				 * (GrinderRecipes.instance().getGrindingResult(itemStack2) !=
				 * null) { if (!mergeItemStack(itemStack2, 0, 1, false)) {
				 * return null; } } else if (slotIndex >= sizeInventory &&
				 * slotIndex < sizeInventory+27) // player inventory slots { if
				 * (!mergeItemStack(itemStack2, sizeInventory+27,
				 * sizeInventory+36, false)) { return null; } } else if
				 * (slotIndex >= sizeInventory+27 && slotIndex <
				 * sizeInventory+36 && !mergeItemStack(itemStack2,
				 * sizeInventory+1, sizeInventory+27, false)) // hotbar slots {
				 * return null; }
				 */
			} else if (!mergeItemStack(itemStack2, sizeInventory,
					sizeInventory + 36, false)) {
				return null;
			}

			if (itemStack2.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack2.stackSize == itemStack1.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemStack2);
		}

		return itemStack1;
	}
}

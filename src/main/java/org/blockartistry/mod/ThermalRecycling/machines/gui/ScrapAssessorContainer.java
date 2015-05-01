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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import org.blockartistry.mod.ThermalRecycling.machines.entity.ScrapAssessorTileEntity;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ThermalRecyclerTileEntity;
import org.blockartistry.mod.ThermalRecycling.machines.entity.TileEntityBase;

import cofh.lib.gui.slot.SlotAcceptValid;
import cofh.lib.gui.slot.SlotLocked;

public class ScrapAssessorContainer extends Container {

	TileEntityBase entity;
	int sizeInventory;
	
	public ScrapAssessorContainer(InventoryPlayer inv, IInventory tileEntity) {

		entity = (ScrapAssessorTileEntity) tileEntity;
		sizeInventory = entity.getSizeInventory();

		Slot s = new SlotAcceptValid(entity, ScrapAssessorTileEntity.INPUT,
				56, 34);
		addSlotToContainer(s);

		for (int i = 0; i < ScrapAssessorTileEntity.DISPLAY_SLOTS.length; i++) {

			int oSlot = ScrapAssessorTileEntity.DISPLAY_SLOTS[i];

			int h = (i % 3) * 18 + 106;
			int v = (i / 3) * 18 + 17;

			s = new SlotLocked(entity, oSlot, h, v);
			addSlotToContainer(s);
		}

		s = new SlotAcceptValid(entity, ThermalRecyclerTileEntity.AUGMENT, 33, 34);
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return entity.isUseableByPlayer(playerIn);
	}
}

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

package org.blockartistry.mod.ThermalRecycling.machines.entity;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ScrapAssessorContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ScrapAssessorGui;

public class ScrapAssessorTileEntity extends TileEntityBase {

	public static final int INPUT = 0;
	public static final int CORE = 10;
	public static final int SAMPLE = 11;
	public static final int[] DISPLAY_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	ItemStack oldStack;
	ItemStack oldCore;

	public ScrapAssessorTileEntity() {
		super(GuiIdentifier.SCRAP_ASSESSOR);
		SidedInventoryComponent inv = new SidedInventoryComponent(this, 12);
		inv.setInputRange(0, 1);
		setMachineInventory(inv);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {

		if (slot == CORE && ProcessingCorePolicy.isProcessingCore(stack))
			return true;
		return super.isItemValidForSlot(slot, stack);
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventory) {
		return new ScrapAssessorGui(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {
		return new ScrapAssessorContainer(inventory, this);
	}

	@Override
	public boolean isWhitelisted(ItemStack stack) {
		return ProcessingCorePolicy.canCoreProcess(getStackInSlot(CORE), stack);
	}

	protected boolean isDecompAugmentInstalled() {
		ItemStack augment = getStackInSlot(CORE);
		return augment != null
				&& augment.getItem() == ItemManager.processingCore;
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			ItemStack input = getStackInSlot(INPUT);
			ItemStack core = getStackInSlot(CORE);
			if (input == oldStack && core == oldCore)
				return;

			// The stack changed. Clear out the display.
			for (int i : DISPLAY_SLOTS)
				setInventorySlotContents(i, null);
			setInventorySlotContents(SAMPLE, null);

			// Set our sentinel and check for null
			oldStack = input;
			oldCore = core;
			if (input == null)
				return;

			// Need to project out a new set. Get the information
			// needed.
			List<ItemStack> results = null;
			ItemStack sample = null;
			RecipeData data = RecipeData.get(input);

			if (data != null) {
				sample = data.getInput();
				if (isDecompAugmentInstalled()) {
					results = (data != null) ? data.getOutput() : null;
				}
			} else {
				sample = input.copy();
				sample.stackSize = 1;
			}

			if (sample != null)
				setInventorySlotContents(SAMPLE, sample);

			if (results == null)
				results = ScrappingTables.getScrapPossibilities(core, input);

			if (results != null) {
				for (int i = 0; i < results.size(); i++) {
					setInventorySlotContents(DISPLAY_SLOTS[i], results.get(i));
				}
			}
		}
	}
}

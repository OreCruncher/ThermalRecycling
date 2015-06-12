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

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler.PreviewResult;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler.ScrappingContext;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ScrapAssessorContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ScrapAssessorGui;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;

public final class ScrapAssessorTileEntity extends TileEntityBase {

	public static final int INPUT = 0;
	public static final int CORE = 1;
	public static final int SAMPLE = 2;
	public static final int[] DISPLAY_SLOTS = { 3, 4, 5, 6, 7, 8, 9, 10, 11 };

	private ItemStack oldStack;
	private ItemStack oldCore;

	public ScrapAssessorTileEntity() {
		super(GuiIdentifier.SCRAP_ASSESSOR);
		final SidedInventoryComponent inv = new SidedInventoryComponent(this,
				12);
		inv.setInputRange(0, 1).setHiddenSlots(CORE);
		setMachineInventory(inv);
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {

		if(slot == INPUT) {
			return CoreType.canCoreProcess(inventory.getStackInSlot(CORE), stack);
		}
			
		if (slot == CORE) {
			return CoreType.isProcessingCore(stack);
		}

		return false;
	}
	
	@Override
	public Object getGuiClient(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new ScrapAssessorGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new ScrapAssessorContainer(inventory, this);
	}

	@Override
	public boolean isWhitelisted(final int slot, final ItemStack stack) {
		
		if (slot == INPUT) {
			return CoreType.canCoreProcess(inventory.getStackInSlot(CORE), stack);
		}
		
		if (slot == CORE) {
			return CoreType.isProcessingCore(stack);
		}

		return false;
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			final ItemStack input = inventory.getStackInSlot(INPUT);
			final ItemStack core = inventory.getStackInSlot(CORE);

			if (input != oldStack || core != oldCore) {

				// The stack changed. Clear out the display.
				for (final int i : DISPLAY_SLOTS)
					inventory.setInventorySlotContents(i, null);
				inventory.setInventorySlotContents(SAMPLE, null);

				// Set our sentinel and check for null
				oldStack = input;
				oldCore = core;
				if (input != null) {

					final ScrappingContext context = new ScrappingContext(core,
							input);
					final PreviewResult result = context.preview();

					inventory.setInventorySlotContents(SAMPLE,
							result.inputRequired);

					if (result.outputGenerated != null) {
						// Cap the output in case the result buffer is larger
						// than
						// what the 3x3 grid can show
						final int maxUpperSlot = Math.min(
								result.outputGenerated.size(),
								DISPLAY_SLOTS.length);
						for (int i = 0; i < maxUpperSlot; i++) {
							inventory.setInventorySlotContents(
									DISPLAY_SLOTS[i],
									result.outputGenerated.get(i));
						}
					}
				}
			}

			inventory.flush();
		}
	}

	@Override
	public void flush() {
		inventory.flush();
	}
}

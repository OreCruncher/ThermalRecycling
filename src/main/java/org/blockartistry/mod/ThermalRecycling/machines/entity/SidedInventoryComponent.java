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

import java.util.Arrays;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import com.google.common.base.Preconditions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public final class SidedInventoryComponent implements IMachineInventory {

	protected final TileEntityBase entity;
	protected final ItemStack[] inventory;

	protected int inputStart = -1;
	protected int inputEnd = -1;
	protected int outputStart = -1;
	protected int outputEnd = -1;

	protected int[] accessibleSlots;
	protected int[] hiddenSlots;
	
	protected boolean isDirty;

	public SidedInventoryComponent(final TileEntityBase parent, final int size) {

		Preconditions.checkNotNull(parent);
		Preconditions.checkArgument(size > 0);

		entity = parent;
		inventory = new ItemStack[size];
	}

	protected int[] getAccessibleSlots() {
		if (accessibleSlots != null)
			return accessibleSlots;

		int size = 0;
		if (inputStart != -1)
			size = inputEnd - inputStart + 1;
		if (outputStart != -1)
			size += outputEnd - outputStart + 1;

		if (size == 0)
			return null;

		accessibleSlots = new int[size];
		int index = 0;
		if (inputStart != -1)
			for (int x = inputStart; x <= inputEnd; x++)
				accessibleSlots[index++] = x;
		if (outputStart != -1)
			for (int x = outputStart; x <= outputEnd; x++)
				accessibleSlots[index++] = x;

		return accessibleSlots;
	}

	protected boolean isInputSlot(final int slot) {
		return inputStart != -1 && slot >= inputStart && slot <= inputEnd;
	}

	protected boolean isOutputSlot(final int slot) {
		return outputStart != -1 && slot >= outputStart && slot <= outputEnd;
	}

	protected void validateRangesDisjoint() {

		if (outputStart == -1 || inputStart == -1)
			return;

		Preconditions.checkState(
				!(inputStart < outputEnd && outputStart < inputEnd),
				"Input and output ranges overlap");
	}

	public SidedInventoryComponent setInputRange(final int start, final int length) {

		inputStart = start;
		inputEnd = start + length - 1;

		validateRangesDisjoint();

		return this;
	}

	public SidedInventoryComponent setOutputRange(final int start, final int length) {

		outputStart = start;
		outputEnd = start + length - 1;

		validateRangesDisjoint();

		return this;
	}

	public SidedInventoryComponent setHiddenSlots(final int... slots) {
		hiddenSlots = slots;
		return this;
	}

	@Override
	public boolean isStackAlreadyInSlot(final int slot, final ItemStack stack) {

		return stack != null && inventory[slot] != null
				&& stack.isItemEqual(inventory[slot])
				&& ItemStack.areItemStackTagsEqual(stack, inventory[slot]);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(final int index, final int count) {

		ItemStack stack = null;
		if (inventory[index] != null) {

			if (inventory[index].stackSize <= count) {
				stack = inventory[index];
				inventory[index] = null;
			} else {
				stack = inventory[index].splitStack(count);

				if (inventory[index].stackSize == 0) {
					inventory[index] = null;
				}
			}
			
			isDirty = true;
		}
		
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack stack) {

		isDirty = true;
		inventory[index] = stack;

		/*
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		// if input slot reset progress
		if (isInputSlot(index) && !isStackAlreadyInSlot(index, stack)) {
			markDirty();
		}
		*/
	}

	@Override
	public String getInventoryName() {
		return "container." + entity.getBlockType().getUnlocalizedName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return player.getDistanceSq(entity.xCoord + 0.5D, entity.yCoord + 0.5D,
				entity.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return isInputSlot(slot) && entity.isWhitelisted(stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int side) {
		return getAccessibleSlots();
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack stack, final int facing) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack stack, final int facing) {
		return isOutputSlot(slot);
	}

	@Override
	public void markDirty() {
		isDirty = true;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {

		Arrays.fill(inventory, null);
		final NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			final NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
			final byte b0 = nbtTagCompound.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length) {
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		final NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				final NBTTagCompound nbtTagCompound = new NBTTagCompound();
				nbtTagCompound.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbtTagCompound);
				nbttaglist.appendTag(nbtTagCompound);
			}
		}

		nbt.setTag("Items", nbttaglist);
	}

	@Override
	public boolean addStackToOutput(final ItemStack stack) {
		isDirty = true;
		return ItemStackHelper.addItemStackToInventory(inventory, stack,
				outputStart, outputEnd - outputStart + 1);
	}

	@Override
	public void dropInventory(final World world, final int x, final int y, final int z) {

		isDirty = true;
		for (final int i : getAccessibleSlots()) {
			final ItemStack stack = getStackInSlot(i);
			if (stack != null)
				ItemStackHelper.spawnIntoWorld(world, stack, x, y, z);
		}

		if (hiddenSlots != null)
			for (final int i : hiddenSlots) {
				final ItemStack stack = getStackInSlot(i);
				if (stack != null)
					ItemStackHelper.spawnIntoWorld(world, stack, x, y, z);
			}

		Arrays.fill(inventory, null);
	}
	
	@Override
	public void flush() {
		if(isDirty) {
			isDirty = false;
			entity.markDirty();
		}
	}
}

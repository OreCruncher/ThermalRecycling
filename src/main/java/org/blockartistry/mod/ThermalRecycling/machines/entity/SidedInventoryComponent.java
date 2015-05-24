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

	TileEntityBase entity;
	ItemStack[] inventory;

	int inputStart = -1;
	int inputEnd = -1;
	int outputStart = -1;
	int outputEnd = -1;

	int[] accessibleSlots;
	int[] hiddenSlots;

	public SidedInventoryComponent(TileEntityBase parent, int size) {

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

	protected boolean isInputSlot(int slot) {
		return inputStart != -1 && slot >= inputStart && slot <= inputEnd;
	}

	protected boolean isOutputSlot(int slot) {
		return outputStart != -1 && slot >= outputStart && slot <= outputEnd;
	}

	protected void validateRangesDisjoint() {

		if (outputStart == -1 || inputStart == -1)
			return;

		Preconditions.checkState(
				!(inputStart < outputEnd && outputStart < inputEnd),
				"Input and output ranges overlap");
	}

	public SidedInventoryComponent setInputRange(int start, int length) {

		inputStart = start;
		inputEnd = start + length - 1;

		validateRangesDisjoint();

		return this;
	}

	public SidedInventoryComponent setOutputRange(int start, int length) {

		outputStart = start;
		outputEnd = start + length - 1;

		validateRangesDisjoint();

		return this;
	}

	public SidedInventoryComponent setHiddenSlots(int... slots) {
		hiddenSlots = slots;
		return this;
	}

	@Override
	public boolean isStackAlreadyInSlot(int slot, ItemStack stack) {

		return stack != null && inventory[slot] != null
				&& stack.isItemEqual(inventory[slot])
				&& ItemStack.areItemStackTagsEqual(stack, inventory[slot]);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		if (inventory[index] != null) {

			ItemStack stack;

			if (inventory[index].stackSize <= count) {
				stack = inventory[index];
				inventory[index] = null;
				return stack;
			} else {
				stack = inventory[index].splitStack(count);

				if (inventory[index].stackSize == 0) {
					inventory[index] = null;
				}

				return stack;
			}

		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		inventory[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		// if input slot reset progress
		if (isInputSlot(index) && !isStackAlreadyInSlot(index, stack)) {
			markDirty();
		}
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
	public boolean isUseableByPlayer(EntityPlayer player) {
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isInputSlot(slot) && entity.isWhitelisted(stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return getAccessibleSlots();
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int facing) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int facing) {
		return isOutputSlot(slot);
	}

	@Override
	public void markDirty() {
		entity.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		inventory = new ItemStack[inventory.length];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbtTagCompound.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length) {
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] != null) {
				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				nbtTagCompound.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbtTagCompound);
				nbttaglist.appendTag(nbtTagCompound);
			}
		}

		nbt.setTag("Items", nbttaglist);
	}

	@Override
	public boolean addStackToOutput(ItemStack stack) {
		return ItemStackHelper.addItemStackToInventory(inventory, stack,
				outputStart, outputEnd - outputStart + 1);
	}

	@Override
	public void dropInventory(World world, int x, int y, int z) {

		for (int i : getAccessibleSlots()) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null)
				ItemStackHelper.spawnIntoWorld(world, stack, x, y, z);
		}

		if (hiddenSlots != null)
			for (int i : hiddenSlots) {
				ItemStack stack = getStackInSlot(i);
				if (stack != null)
					ItemStackHelper.spawnIntoWorld(world, stack, x, y, z);
			}

		Arrays.fill(inventory, null);
	}
}

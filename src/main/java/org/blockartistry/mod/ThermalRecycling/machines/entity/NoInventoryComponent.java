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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public final class NoInventoryComponent implements IMachineInventory {
	
	private final static int[] empty = new int[0];

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(final int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
	}

	@Override
	public String getInventoryName() {
		return "container.empty";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return empty;
	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_,
			final int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_,
			final int p_102008_3_) {
		return false;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
	}

	@Override
	public boolean addStackToOutput(final ItemStack stack) {
		return false;
	}

	@Override
	public void dropInventory(final World world, final int x, final int y, final int z) {
	}

	@Override
	public boolean isStackAlreadyInSlot(final int slot, final ItemStack stack) {
		return false;
	}
}

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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.components.InternalEnergyStorage;
import org.blockartistry.mod.ThermalRecycling.machines.components.OperationTracker;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerGui;
import org.blockartistry.mod.ThermalRecycling.util.INBTSerializer;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ThermalRecyclerTileEntity extends TileEntityBase implements
		ISidedInventory, IEnergyReceiver, IEnergyInfo, INBTSerializer {

	// Slot geometry for the machine
	public static final int INPUT = 0;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	protected ItemStack[] inventory = new ItemStack[INPUT_SLOTS.length
			+ OUTPUT_SLOTS.length];

	// Entity state that needs to be serialized
	protected InternalEnergyStorage energyStorage;
	protected OperationTracker operationTracker;
	protected String customName;

	// Entity state that is transient
	static int ENERGY_PER_OPERATION = 4000;
	static int ENERGY_PER_TICK = 40;

	public ThermalRecyclerTileEntity() {
		energyStorage = new InternalEnergyStorage(32000);
		operationTracker = new OperationTracker(ENERGY_PER_TICK,
				ENERGY_PER_OPERATION);
	}

	public int getProgress() {
		return operationTracker.percentComplete();
	}
	
	// /////////////////////////////////////
	//
	// INBTSerializer
	//
	// /////////////////////////////////////

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);
		operationTracker.readFromNBT(nbt);

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbtTagCompound = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbtTagCompound.getByte("Slot");

			if (b0 >= 0 && b0 < inventory.length) {
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
			}
		}

		if (nbt.hasKey("CustomName", 8)) {
			customName = nbt.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
		operationTracker.writeToNBT(nbt);

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

		if (hasCustomInventoryName()) {
			nbt.setString("CustomName", customName);
		}
	}

	// /////////////////////////////////////
	//
	// IEnergyReceiver
	//
	// /////////////////////////////////////

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return energyStorage.canConnectEnergy(from);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energyStorage.getEnergyStored(from);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return energyStorage.getMaxEnergyStored(from);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxRecieve,
			boolean simulate) {
		return energyStorage.receiveEnergy(from, maxRecieve, simulate);
	}

	// /////////////////////////////////////
	//
	// IEnergyInfo
	//
	// /////////////////////////////////////

	@Override
	public int getInfoEnergyPerTick() {
		return operationTracker.isActive() ? operationTracker
				.energyRequiredForTick() : 0;
	}

	@Override
	public int getInfoEnergyStored() {
		return energyStorage.getEnergyStored(ForgeDirection.DOWN);
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return ENERGY_PER_TICK;
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored(ForgeDirection.DOWN);
	}

	// /////////////////////////////////////
	//
	// TileEntityBase
	//
	// /////////////////////////////////////

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float a, float b, float c) {

		if (!world.isRemote) {
			player.openGui(ThermalRecycling.MOD_ID, 0, world, x, y, z);
		}

		return false;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventory) {
		return new ThermalRecyclerGui(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {
		return new ThermalRecyclerContainer(inventory, this);
	}

	// /////////////////////////////////////
	//
	// ISidedInventory
	//
	// /////////////////////////////////////

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
			ItemStack itemstack;

			if (inventory[index].stackSize <= count) {
				itemstack = inventory[index];
				inventory[index] = null;
				return itemstack;
			} else {
				itemstack = inventory[index].splitStack(count);

				if (inventory[index].stackSize == 0) {
					inventory[index] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (inventory[index] != null) {
			ItemStack itemstack = inventory[index];
			inventory[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		boolean isSameItemStackAlreadyInSlot = stack != null && inventory[index] != null
				&& stack.isItemEqual(inventory[index])
				&& ItemStack.areItemStackTagsEqual(stack, inventory[index]);

		inventory[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		// if input slot, reset the grinding timers
		if (index == INPUT && !isSameItemStackAlreadyInSlot) {
			operationTracker.start();
			markDirty();
		}
	}

	@Override
	public String getInventoryName() {
		return "container.thermalRecycler";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	/*
	 * 
	 * @Override public String getName() { return hasCustomName() ?
	 * grinderCustomName : "container.grinder"; }
	 * 
	 * @Override public boolean hasCustomName() { return grinderCustomName !=
	 * null && grinderCustomName.length() > 0; }
	 * 
	 * public void setCustomInventoryName(String parCustomName) {
	 * grinderCustomName = parCustomName; }
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player
				.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == INPUT;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return OUTPUT_SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int facing) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int facing) {
		return true;
	}
}

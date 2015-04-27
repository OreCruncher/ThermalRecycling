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

import java.util.ArrayList;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.MachineBase;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerGui;
import org.blockartistry.mod.ThermalRecycling.util.INBTSerializer;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import cofh.lib.util.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ThermalRecyclerTileEntity extends TileEntityBase implements
		ISidedInventory, IEnergyReceiver, IEnergyInfo, IJobProgress, INBTSerializer {

	// Update actions
	public static final int UPDATE_ACTION_ENERGY = 0;
	public static final int UPDATE_ACTION_PROGRESS = 1;
	public static final int UPDATE_ACTION_ENERGY_RATE = 2;

	// Slot geometry for the machine
	public static final int INPUT = 0;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int[] ALL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	protected ItemStack[] inventory = new ItemStack[INPUT_SLOTS.length
			+ OUTPUT_SLOTS.length];

	// Internal buffer for funneling results into output grid. This is
	// in the case of when the output grid gets filled and there is no
	// more room to toss stuff.
	protected ItemStack[] buffer = new ItemStack[OUTPUT_SLOTS.length];

	// Entity state that needs to be serialized
	protected int energy = 0;
	protected int progress = 0;
	protected int energyRate = 0;

	static final int ENERGY_MAX_STORAGE = 32000;
	static final int ENERGY_PER_OPERATION = 2000;
	static final int ENERGY_PER_TICK = 40;
	static final int ENERGY_MAX_RECEIVE = ENERGY_PER_TICK * 3;
	static final int RECYCLE_DUST_CHANCE = 25;
	
	static boolean isJammed = false;

	public ThermalRecyclerTileEntity() {
	}

	// /////////////////////////////////////
	//
	// Syncronization logic across client/server
	//
	// /////////////////////////////////////

	public int getProgress() {
		return progress;
	}

	@Override
	public boolean receiveClientEvent(int action, int param) {

		if (!worldObj.isRemote)
			return true;

		switch (action) {
		case UPDATE_ACTION_ENERGY:
			energy = param;
			break;
		case UPDATE_ACTION_PROGRESS:
			progress = param;
			break;
		case UPDATE_ACTION_ENERGY_RATE:
			energyRate = param;
			break;
		default:
			;
		}

		return true;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		// Initializing to a base state from the server
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket() {
		// Sends out the base state to clients
		NBTTagCompound syncData = new NBTTagCompound();
		writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, syncData);
	}


	// /////////////////////////////////////
	//
	// IJobProgress
	//
	// /////////////////////////////////////

	@Override
	public int getPercentComplete() {
		return (progress * 100) / ENERGY_PER_OPERATION;
	}

	@Override
	public boolean isActive() {
		return inventory[INPUT] != null;
	}

	@Override
	public boolean isJammed() {
		return isJammed;
	}

	// /////////////////////////////////////
	//
	// INBTSerializer
	//
	// /////////////////////////////////////

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energy = nbt.getShort("energy");
		energyRate = nbt.getShort("energyRate");
		progress = nbt.getShort("progress");

		NBTTagList nbttaglist = nbt.getTagList("buffer", 10);
		if (nbttaglist.tagCount() > 0) {
			buffer = new ItemStack[nbttaglist.tagCount()];
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				buffer[i] = ItemStack.loadItemStackFromNBT(nbttaglist
						.getCompoundTagAt(i));
			}
		}

		nbttaglist = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];

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
		super.writeToNBT(nbt);
		nbt.setShort("energy", (short) energy);
		nbt.setShort("energyRate", (short) energyRate);
		nbt.setShort("progress", (short) progress);

		NBTTagList nbttaglist = new NBTTagList();

		if (buffer != null) {
			for (int i = 0; i < buffer.length; ++i) {
				if (buffer[i] != null) {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					buffer[i].writeToNBT(nbtTagCompound);
					nbttaglist.appendTag(nbtTagCompound);
				}
			}
		}

		nbt.setTag("buffer", nbttaglist);

		nbttaglist = new NBTTagList();
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

	// /////////////////////////////////////
	//
	// IEnergyReceiver
	//
	// /////////////////////////////////////

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return ENERGY_MAX_STORAGE;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxRecieve,
			boolean simulate) {

		int result = Math.min(maxRecieve, ENERGY_MAX_RECEIVE);

		if ((energy + result) > ENERGY_MAX_STORAGE) {
			result = ENERGY_MAX_STORAGE - energy;
		}

		if (!simulate) {
			energy += result;
		}

		return result;
	}

	// /////////////////////////////////////
	//
	// IEnergyInfo
	//
	// /////////////////////////////////////

	@Override
	public int getInfoEnergyPerTick() {
		return (progress != -1 && progress != 100) ? Math.min(ENERGY_PER_TICK,
				energy) : 0;
	}

	@Override
	public int getInfoEnergyStored() {
		return energy;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return ENERGY_PER_TICK;
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return ENERGY_MAX_STORAGE;
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

		return true;
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

		boolean isSameItemStackAlreadyInSlot = stack != null
				&& inventory[index] != null
				&& stack.isItemEqual(inventory[index])
				&& ItemStack.areItemStackTagsEqual(stack, inventory[index]);

		inventory[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		// if input slot reset progress
		if (index == INPUT && !isSameItemStackAlreadyInSlot) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			progress = 0;
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
		return slot == INPUT
				&& !RecipeManager.isThermalRecyclerBlacklisted(stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return ALL_SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int facing) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int facing) {
		return slot != INPUT;
	}
	
	// Toggles the machine meta data so that it is considered active.
	// This will result in the active face being displayed as well as
	// have a little bit of light.
	protected void setMachineActive(boolean toggle) {
		if(!worldObj.isRemote) {
			
			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			int temp = meta;
			
			if(toggle)
				meta |= MachineBase.META_ACTIVE_INDICATOR;
			else
				meta &= ~MachineBase.META_ACTIVE_INDICATOR;
			
			if(meta != temp)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
		}
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			// If we have residual items in the buffer, attempt
			// to flush. If not successful, return. We have more
			// items in the buffer and processing will not continue
			// until the internal buffer is empty.
			if (!flushBuffer()) {
				setMachineActive(false);
				energyRate = 0;
				return;
			}

			// If there is nothing to process, then there is nothing
			// to do.
			if (!hasItemToRecycle()) {
				setMachineActive(false);
				progress = 0;
				energyRate = 0;
				return;
			}

			// If work is still progressing, tick
			if (progress < ENERGY_PER_OPERATION) {
				if (energy >= ENERGY_PER_TICK) {
					energy -= ENERGY_PER_TICK;
					progress += ENERGY_PER_TICK;
					energyRate = ENERGY_PER_TICK;
					setMachineActive(true);
				} else {
					setMachineActive(false);
					energyRate = 0;
				}
			}

			// We are done. Right now just move to an output slot
			if (progress >= ENERGY_PER_OPERATION) {
				if (recycleItem()) {
					progress = 0;
				}
			}
		}
	}

	protected boolean hasItemToRecycle() {
		return inventory[INPUT] != null;
	}

	protected boolean flushBuffer() {

		if (buffer == null)
		{
			isJammed = false;
			return true;
		}

		boolean isEmpty = true;

		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] != null) {
				if (InventoryHelper.addItemStackToInventory(inventory,
						buffer[i], 1, OUTPUT_SLOTS.length)) {
					buffer[i] = null;
				} else {
					isEmpty = false;
				}
			}
		}

		if (isEmpty)
			buffer = null;
		else
			isJammed = true;
		
		markDirty();

		return isEmpty;
	}

	protected boolean recycleItem() {

		// Sanity check...
		if (inventory[INPUT] == null)
			return true;

		// Decrement our input slot. The decrStackSize
		// method will handle appropriate nulling of
		// inventory slots when count goes to 0.
		ItemStack justRecycled = decrStackSize(INPUT, 1);

		// Get the results of recycling. The stacks already
		// have been cloned so they are safe to hold onto.
		buffer = RecipeManager.getResultStacks(justRecycled);

		// If there isn't a recipe defined, generate some
		// recycling scrap consolation prizes.
		if (buffer == null)
			buffer = getConsolationPrizes();
		else
			buffer = breakItDown(buffer);

		// Flush the generated stacks into the output buffer
		return flushBuffer();
	}

	// Generate a list of results in the case an item does not have
	// a recipe. Chances are it will be a bunch of dust.
	protected ItemStack[] getConsolationPrizes() {
		ItemStack[] result = new ItemStack[1];
		result[0] = new ItemStack(ItemManager.recyclingScrap, 1, 2);
		return result;
	}

	// Iterate through the results of the recipe randomly
	// turning stuff into dust.
	protected ItemStack[] breakItDown(ItemStack[] in) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		Random rnd = new Random();

		for (int i = 0; i < in.length; i++) {

			int count = in[i].stackSize;
			int dustAmount = 0;

			for (int j = 0; j < count; j++)
				if (rnd.nextInt(100) < RECYCLE_DUST_CHANCE)
					dustAmount++;

			if (dustAmount == 0)
				result.add(in[i]);
			else {
				result.add(new ItemStack(ItemManager.recyclingScrap,
						dustAmount, 2));
				if (dustAmount != count) {
					in[i].stackSize -= dustAmount;
					result.add(in[i]);
				}

			}
		}

		return result.toArray(new ItemStack[result.size()]);
	}
}

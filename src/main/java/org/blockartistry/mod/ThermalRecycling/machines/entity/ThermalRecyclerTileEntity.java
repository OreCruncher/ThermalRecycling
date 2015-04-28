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
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerGui;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ThermalRecyclerTileEntity extends TileEntityBase implements
		IEnergyReceiver, IEnergyInfo, IJobProgress {

	// Update actions
	public static final int UPDATE_ACTION_ENERGY = 0;
	public static final int UPDATE_ACTION_PROGRESS = 1;
	public static final int UPDATE_ACTION_ENERGY_RATE = 2;

	// Slot geometry for the thermalRecycler
	public static final int INPUT = 0;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int[] ALL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	// Internal buffer for funneling results into output grid. This is
	// in the case of when the output grid gets filled and there is no
	// more room to toss stuff.
	protected ItemStack[] buffer = new ItemStack[OUTPUT_SLOTS.length];

	// Entity state that needs to be serialized
	protected int energy = 0;
	protected int progress = 0;
	protected int energyRate = 0;

	static final int ENERGY_MAX_STORAGE = 32000;
	static final int ENERGY_PER_OPERATION = 2400;
	static final int ENERGY_PER_TICK = 40;
	static final int ENERGY_MAX_RECEIVE = ENERGY_PER_TICK * 3;
	static final int RECYCLE_DUST_CHANCE = 25;

	static boolean isJammed = false;

	public ThermalRecyclerTileEntity() {
		SidedInventoryComponent inv = new SidedInventoryComponent(this, 10);
		inv.setInputRange(0, 1).setOutputRange(1, 9);
		setMachineInventory(inv);
	}

	@Override
	public boolean isWhitelisted(ItemStack stack) {
		return ThermalRecyclerTables.isThermalRecyclerWhitelisted(stack);
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
		return this.getStackInSlot(INPUT) != null;
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
		return isActive() ? Math.min(ENERGY_PER_TICK, energy) : 0;
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

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			// worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

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
		return getStackInSlot(INPUT) != null;
	}

	protected boolean flushBuffer() {

		if (buffer == null) {
			isJammed = false;
			return true;
		}

		boolean isEmpty = true;

		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] != null) {
				if (addStackToOutput(buffer[i])) {
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
		if (!hasItemToRecycle())
			return true;

		// Decrement our input slot. The decrStackSize
		// method will handle appropriate nulling of
		// inventory slots when count goes to 0.
		ItemStack justRecycled = decrStackSize(INPUT, 1);

		// Get the results of recycling. The stacks already
		// have been cloned so they are safe to hold onto.
		buffer = ThermalRecyclerTables.getResultStacks(justRecycled);

		// If there isn't a recipe defined, generate some
		// recycling scrap consolation prizes.
		if (buffer == null) {
			ItemStack cupieDoll = ThermalRecyclerTables
					.pickStackFrom(ThermalRecyclerTables.unknownScrap);
			if (cupieDoll != null)
				buffer = new ItemStack[] { cupieDoll };
		} else
			buffer = breakItDown(buffer);

		// Flush the generated stacks into the output buffer
		return flushBuffer();
	}

	// Iterate through the results of the recipe randomly
	// turning stuff into dust.
	protected ItemStack[] breakItDown(ItemStack[] in) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = 0; i < in.length; i++) {

			int count = in[i].stackSize;

			for (int j = 0; j < count; j++) {
				ItemStack cupieDoll = ThermalRecyclerTables
						.pickStackFrom(ThermalRecyclerTables.componentScrap);

				// Fix this compare!
				if (cupieDoll != null
						&& ThermalRecyclerTables.keep.isItemEqual(cupieDoll))
					continue;

				// Reduce stack size by one
				in[i].stackSize--;

				// If we have a result add it to the list
				if (cupieDoll != null)
					result.add(cupieDoll);
			}

			// If we have anything left over keep
			if (in[i].stackSize > 0)
				result.add(in[i]);
		}

		return result.toArray(new ItemStack[result.size()]);
	}
}

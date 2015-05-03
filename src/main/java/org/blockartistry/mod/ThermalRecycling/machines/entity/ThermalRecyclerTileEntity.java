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
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.MachineStatus;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerGui;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

public class ThermalRecyclerTileEntity extends TileEntityBase implements
		IEnergyReceiver, IEnergyInfo, IJobProgress {

	// Update actions
	public static final int UPDATE_ACTION_ENERGY = 0;
	public static final int UPDATE_ACTION_PROGRESS = 1;
	public static final int UPDATE_ACTION_ENERGY_RATE = 2;
	public static final int UPDATE_ACTION_STATUS = 3;

	// Slot geometry for the thermalRecycler
	public static final int INPUT = 0;
	public static final int CORE = 10;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int[] ALL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	// Internal buffer for funneling results into output grid. This is
	// in the case of when the output grid gets filled and there is no
	// more room to toss stuff.
	protected List<ItemStack> buffer;

	// Entity state that needs to be serialized
	protected int energy = 0;
	protected int progress = 0;
	protected int energyRate = 0;

	static final int ENERGY_MAX_STORAGE = 32000;
	static final int ENERGY_PER_OPERATION = 2400;
	static final int ENERGY_PER_TICK = 40;
	static final int ENERGY_MAX_RECEIVE = ENERGY_PER_TICK * 3;
	static final int RECYCLE_DUST_CHANCE = 25;

	protected MachineStatus status = MachineStatus.IDLE;

	public ThermalRecyclerTileEntity() {
		super(GuiIdentifier.THERMAL_RECYCLER);
		SidedInventoryComponent inv = new SidedInventoryComponent(this, 11);
		inv.setInputRange(0, 1).setOutputRange(1, 9);
		setMachineInventory(inv);
	}

	@Override
	public boolean isWhitelisted(ItemStack stack) {
		return ProcessingCorePolicy.canCoreProcess(getStackInSlot(CORE), stack);
	}

	// /////////////////////////////////////
	//
	// Syncronization logic across client/server
	//
	// /////////////////////////////////////

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
		case UPDATE_ACTION_STATUS:
			status = MachineStatus.map(param);
			break;
		default:
			;
		}

		return true;
	}

	public int getProgress() {
		return progress;
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
	public MachineStatus getStatus() {
		return status;
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
		status = MachineStatus.map(nbt.getShort("status"));

		NBTTagList nbttaglist = nbt.getTagList("buffer", 10);
		if (nbttaglist.tagCount() > 0) {
			buffer = new ArrayList<ItemStack>();
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				buffer.add(ItemStack.loadItemStackFromNBT(nbttaglist
						.getCompoundTagAt(i)));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setShort("energy", (short) energy);
		nbt.setShort("energyRate", (short) energyRate);
		nbt.setShort("progress", (short) progress);
		nbt.setShort("status", (short)status.ordinal());

		NBTTagList nbttaglist = new NBTTagList();

		if (buffer != null) {
			for (ItemStack stack: buffer) {
				if (stack != null) {
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					stack.writeToNBT(nbtTagCompound);
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
		return getStatus() == MachineStatus.ACTIVE ? Math.min(ENERGY_PER_TICK,
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {

		if(slot == CORE && ProcessingCorePolicy.isProcessingCore(stack))
			return true;
		return super.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
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

			ItemStack inputSlotStack = getStackInSlot(INPUT);

			switch (status) {

			case IDLE:
				progress = 0;
				if (inputSlotStack != null)
					status = MachineStatus.ACTIVE;
				break;

			case ACTIVE:
				if (energy < ENERGY_PER_TICK) {
					status = MachineStatus.OUT_OF_POWER;
				} else if (inputSlotStack == null) {
					status = MachineStatus.IDLE;
				} else if (!hasItemToRecycle()) {
					progress = 0;
					status = MachineStatus.NEED_MORE_RESOURCES;
				} else {

					if (progress >= ENERGY_PER_OPERATION) {
						progress = 0;

						if (!recycleItem()) {
							status = MachineStatus.JAMMED;
						}

					} else {
						progress += ENERGY_PER_TICK;
						energy -= ENERGY_PER_TICK;
					}

					setMachineActive(true);
				}

				break;

			case JAMMED:
				if (flushBuffer())
					status = MachineStatus.ACTIVE;
				break;

			case NEED_MORE_RESOURCES:
				if (inputSlotStack == null)
					status = MachineStatus.IDLE;
				else if (hasItemToRecycle())
					status = MachineStatus.ACTIVE;
				break;

			case OUT_OF_POWER:
				if (inputSlotStack == null)
					status = MachineStatus.IDLE;
				else if (energy >= ENERGY_PER_TICK)
					status = MachineStatus.ACTIVE;
				break;

			default:
				break;
			}

			if (status != MachineStatus.ACTIVE) {
				energyRate = 0;
				setMachineActive(false);
			}
		}
	}

	protected boolean hasItemToRecycle() {
		ItemStack input = getStackInSlot(INPUT);
		if (input == null)
			return false;

		int quantityRequired = RecipeData.getMinimumQuantityToRecycle(input);
		boolean result = (quantityRequired != -1)
				&& quantityRequired <= input.stackSize;

		return result;
	}

	protected boolean flushBuffer() {

		if (buffer == null) {
			return true;
		}

		boolean isEmpty = true;

		for(int i = 0; i < buffer.size(); i++) {
			ItemStack stack = buffer.get(i);
			if (stack != null) {
				if (addStackToOutput(stack)) {
					buffer.set(i, null);
				} else {
					isEmpty = false;
				}
			}
		}

		if (isEmpty)
			buffer = null;

		markDirty();

		return isEmpty;
	}
	
	protected boolean isDecompositionCoreInstalled() {
		return ProcessingCorePolicy.isDecompositionCore(getStackInSlot(CORE));
	}
	
	protected boolean isExtractionCoreInstalled() {
		return ProcessingCorePolicy.isExtractionCore(getStackInSlot(CORE));
	}

	protected boolean recycleItem() {

		// Get how many items we need to snag off the stack
		int quantityRequired = RecipeData
				.getMinimumQuantityToRecycle(getStackInSlot(INPUT));
		if (quantityRequired < 1)
			return true;

		// Decrement our input slot. The decrStackSize
		// method will handle appropriate nulling of
		// inventory slots when count goes to 0.
		ItemStack justRecycled = decrStackSize(INPUT, quantityRequired);
		
		buffer = ScrappingTables.scrapItems(getStackInSlot(CORE), justRecycled);

		// Flush the generated stacks into the output buffer
		return flushBuffer();
	}
}

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
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.client.ParticleEffects;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class ThermalRecyclerTileEntity extends TileEntityBase implements
		IEnergyReceiver, IEnergyInfo, IJobProgress {

	// Update actions
	public static final int UPDATE_ACTION_ENERGY = 0;
	public static final int UPDATE_ACTION_PROGRESS = 1;
	public static final int UPDATE_ACTION_ENERGY_RATE = 2;
	public static final int UPDATE_ACTION_STATUS = 3;

	// Slot geometry for the machine
	public static final int INPUT = 0;
	public static final int CORE = 10;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int[] ALL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	static final int ENERGY_MAX_STORAGE = 60000;
	static final int ENERGY_PER_TICK = 40;
	static final int ENERGY_MAX_RECEIVE = ENERGY_PER_TICK * 3;

	static final int ENERGY_PER_OPERATION_SCRAP = 800;
	static final int ENERGY_PER_OPERATION_DECOMP = 1600;
	static final int ENERGY_PER_OPERATION_EXTRACT = 3200;

	// Entity state that needs to be serialized
	protected int energy = 0;
	protected int progress = 0;
	protected int energyRate = 0;
	protected MachineStatus status = MachineStatus.IDLE;
	protected List<ItemStack> buffer;

	// Transient state to increase performance
	protected ItemStack activeStack;
	protected RecipeData activeRecipe;
	
	public ThermalRecyclerTileEntity() {
		super(GuiIdentifier.THERMAL_RECYCLER);
		final SidedInventoryComponent inv = new SidedInventoryComponent(this, 11);
		inv.setInputRange(0, 1).setOutputRange(1, 9).setHiddenSlots(CORE);
		setMachineInventory(inv);
	}

	/**
	 * Retrieve the stack currently in the input slot.  The RecipeData is
	 * retrieved for the stack if it has not already been retrieved.  Goal
	 * is to keep the data cached and avoid repeated lookups each tick.
	 */
	protected ItemStack detectInputStack() {
		final ItemStack input = getStackInSlot(INPUT);
		if(activeStack != input) {
			activeStack = input;
			if(input != null) {
				activeRecipe = RecipeData.get(input);
			} else {
				activeRecipe = null;
			}
		}
		return input;
	}
	
	// Energy characteristics of the machine
	protected static int operationEnergyForCore(final ItemStack core) {
		if (core == null)
			return ENERGY_PER_OPERATION_SCRAP;

		if (ProcessingCorePolicy.isExtractionCore(core))
			return ENERGY_PER_OPERATION_EXTRACT;

		if (ProcessingCorePolicy.isDecompositionCore(core))
			return ENERGY_PER_OPERATION_DECOMP;

		// Shouldn't get here....
		return ENERGY_PER_OPERATION_DECOMP;
	}

	@Override
	public boolean isWhitelisted(final ItemStack stack) {
		return ProcessingCorePolicy.canCoreProcess(getStackInSlot(CORE), stack);
	}

	// /////////////////////////////////////
	//
	// Synchronization logic across client/server
	//
	// /////////////////////////////////////

	@Override
	public boolean receiveClientEvent(final int action, final int param) {

		if (!worldObj.isRemote)
			return true;

		switch (action) {
		case UPDATE_ACTION_ENERGY:
			energy = param*10;
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
		return (progress * 100) / operationEnergyForCore(getStackInSlot(CORE));
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
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		energy = nbt.getInteger("energy");
		energyRate = nbt.getShort("energyRate");
		progress = nbt.getShort("progress");
		status = MachineStatus.map(nbt.getShort("status"));

		final NBTTagList nbttaglist = nbt.getTagList("buffer", 10);
		if (nbttaglist.tagCount() > 0) {
			buffer = new ArrayList<ItemStack>(nbttaglist.tagCount());
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				buffer.add(ItemStack.loadItemStackFromNBT(nbttaglist
						.getCompoundTagAt(i)));
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setInteger("energy", energy);
		nbt.setShort("energyRate", (short) energyRate);
		nbt.setShort("progress", (short) progress);
		nbt.setShort("status", (short) status.ordinal());

		final NBTTagList nbttaglist = new NBTTagList();

		if (buffer != null) {
			for (final ItemStack stack : buffer) {
				if (stack != null) {
					final NBTTagCompound nbtTagCompound = new NBTTagCompound();
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
	public boolean canConnectEnergy(final ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(final ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(final ForgeDirection from) {
		return ENERGY_MAX_STORAGE;
	}

	@Override
	public int receiveEnergy(final ForgeDirection from, final int maxRecieve,
			final boolean simulate) {

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
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {

		if (slot == CORE && ProcessingCorePolicy.isProcessingCore(stack))
			return true;
		return super.isItemValidForSlot(slot, stack);
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack stack) {
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public Object getGuiClient(final InventoryPlayer inventory) {
		return new ThermalRecyclerGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final InventoryPlayer inventory) {
		return new ThermalRecyclerContainer(inventory, this);
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			final MachineStatus previousStatus = status;
			final ItemStack inputSlotStack = detectInputStack();

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

					final ItemStack core = getStackInSlot(CORE);
					if (progress >= operationEnergyForCore(core)) {
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

			if(status != previousStatus) {
				final boolean isActive = status == MachineStatus.ACTIVE;
				setMachineActive(isActive);
				if(!isActive) {
					energyRate = 0;
				}
			}
		}
	}

	@Override
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
		if (!ModOptions.getEnableRecyclerFX())
			return;

		if (!(status == MachineStatus.ACTIVE || status == MachineStatus.JAMMED)
				|| rand.nextInt(9) != 0)
			return;

		String particle = "happyVillager";
		if (status == MachineStatus.JAMMED)
			particle = "reddust";

		ParticleEffects.spawnParticlesAroundBlock(particle, world, x, y, z,
				rand);
	}

	protected boolean hasItemToRecycle() {
		return activeRecipe != null && activeRecipe.getMinimumInputQuantityRequired() <= activeStack.stackSize;
	}

	protected boolean flushBuffer() {

		if (buffer == null) {
			return true;
		}

		boolean isEmpty = true;

		for (int i = 0; i < buffer.size(); i++) {
			final ItemStack stack = buffer.get(i);
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

	protected boolean recycleItem() {

		// Get how many items we need to snag off the stack
		final int quantityRequired = activeRecipe.getMinimumInputQuantityRequired();

		// Decrement our input slot. The decrStackSize
		// method will handle appropriate nulling of
		// inventory slots when count goes to 0.
		final ItemStack justRecycled = decrStackSize(INPUT, quantityRequired);

		buffer = ScrappingTables.scrapItems(getStackInSlot(CORE), justRecycled);

		// Flush the generated stacks into the output buffer
		return flushBuffer();
	}
}

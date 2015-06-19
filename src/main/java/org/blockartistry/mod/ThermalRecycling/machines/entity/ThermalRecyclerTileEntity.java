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
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler.ScrappingContext;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingContextCache;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.MachineStatus;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ThermalRecyclerGui;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;

import cpw.mods.fml.common.Optional;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.InterfaceList(value = {
		@Optional.Interface(iface = "cofh.api.energy.IEnergyReciever", modid = "CoFHCore", striprefs = true),
		@Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHCore", striprefs = true), })
public final class ThermalRecyclerTileEntity extends TileEntityBase implements
		IEnergyReceiver, IEnergyInfo, IJobProgress {

	// Update actions
	public static final int UPDATE_ACTION_ENERGY = 10;
	public static final int UPDATE_ACTION_PROGRESS = 11;
	public static final int UPDATE_ACTION_ENERGY_RATE = 12;

	// Slot geometry for the machine
	public static final int INPUT = 0;
	public static final int CORE = 10;
	public static final int[] INPUT_SLOTS = { INPUT };
	public static final int[] OUTPUT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int[] ALL_SLOTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private static final int ENERGY_MAX_STORAGE = 60000;
	private static final int ENERGY_PER_TICK = 40;
	private static final int ENERGY_MAX_RECEIVE = ENERGY_PER_TICK * 3;
	private static final int ENERGY_PER_OPERATION_SCRAP = 800;
	private static final int ENERGY_PER_OPERATION_DECOMP = 1600;
	private static final int ENERGY_PER_OPERATION_EXTRACT = 3200;

	private static final int LRU_CACHE_SIZE = 6;

	private class NBT {
		public static final String ENERGY = "energy";
		public static final String ENERGY_RATE = "energyRate";
		public static final String PROGRESS = "progress";
		public static final String BUFFER = "buffer";
	}

	// Entity state that needs to be serialized
	protected int energy = 0;
	protected int progress = 0;
	protected int energyRate = 0;
	protected List<ItemStack> buffer;

	// Transient state to increase performance. Generally
	// the expectations are that the same operation will
	// occur as the previous one. For example, when
	// processing a full stack the data needs to be
	// collected once, and repeated another 63 times.
	//
	// Also to work better in farm situations the cache will
	// remember the last N contexts that were created.
	// Generally in a farm there is a small handful of items
	// that come through so this LRU cache will help keep
	// performance up.
	protected ScrappingContextCache contextCache = null;
	protected ScrappingContext context;
	protected ItemStack activeStack;
	protected ItemStack activeCore;

	public ThermalRecyclerTileEntity() {
		super(GuiIdentifier.THERMAL_RECYCLER);
		final SidedInventoryComponent inv = new SidedInventoryComponent(this,
				11);
		inv.setInputRange(0, 1).setOutputRange(1, 9).setHiddenSlots(CORE);
		setMachineInventory(inv);
	}

	/**
	 * Retrieve the stack currently in the input slot. Data for the operation is
	 * pulled in and cached. Goal is to keep the data cached and avoid repeated
	 * lookups each tick.
	 */
	protected ItemStack detectInputStack() {

		if (contextCache == null) {
			contextCache = new ScrappingContextCache(LRU_CACHE_SIZE);
		}

		final ItemStack input = inventory.getStackInSlot(INPUT);
		final ItemStack core = inventory.getStackInSlot(CORE);
		if (activeStack != input || activeCore != core) {
			activeStack = input;
			activeCore = core;
			if (input != null) {
				context = contextCache.getContext(core, input);
			} else {
				context = null;
			}
		}
		return input;
	}

	// Energy characteristics of the machine
	protected int operationEnergyForCore(final ItemStack core) {

		switch (CoreType.getType(core)) {
		case DECOMPOSITION:
			return ENERGY_PER_OPERATION_DECOMP;
		case EXTRACTION:
			return ENERGY_PER_OPERATION_EXTRACT;
		case NONE:
		default:
			return ENERGY_PER_OPERATION_SCRAP;
		}
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return slot == INPUT
				|| (slot == CORE && CoreType.isProcessingCore(stack));
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
			energy = param * 10;
			break;
		case UPDATE_ACTION_PROGRESS:
			progress = param;
			break;
		case UPDATE_ACTION_ENERGY_RATE:
			energyRate = param;
			break;
		default:
			return super.receiveClientEvent(action, param);
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
	// These are called client side so have
	// to be careful about where we pull
	// the data - some of it is server side.
	//
	// /////////////////////////////////////

	@Override
	public int getPercentComplete() {
		return (progress * 100)
				/ operationEnergyForCore(inventory.getStackInSlot(CORE));
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

		energy = nbt.getInteger(NBT.ENERGY);
		energyRate = nbt.getShort(NBT.ENERGY_RATE);
		progress = nbt.getShort(NBT.PROGRESS);

		final NBTTagList nbttaglist = nbt.getTagList(NBT.BUFFER, 10);
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

		nbt.setInteger(NBT.ENERGY, energy);
		nbt.setShort(NBT.ENERGY_RATE, (short) energyRate);
		nbt.setShort(NBT.PROGRESS, (short) progress);

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

		nbt.setTag(NBT.BUFFER, nbttaglist);
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
		return status == MachineStatus.ACTIVE ? Math.min(ENERGY_PER_TICK,
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
	public void setInventorySlotContents(final int index, final ItemStack stack) {
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public Object getGuiClient(final GuiIdentifier id,
			final InventoryPlayer inventory) {
		return new ThermalRecyclerGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final GuiIdentifier id,
			final InventoryPlayer inventory) {
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
				if (inputSlotStack != null) {
					if(context.shouldJam) {
						status = MachineStatus.JAMMED;
					} else {
						status = MachineStatus.ACTIVE;
					}
				}
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

					if (progress >= operationEnergyForCore(activeCore)) {
						progress = 0;

						if (!recycleItem()) {
							status = MachineStatus.JAMMED;
						}

					} else {
						progress += ENERGY_PER_TICK;
						energy -= ENERGY_PER_TICK;
					}
				}

				break;

			case JAMMED:
				if ((context == null || !context.shouldJam) && flushBuffer()) {
					if(inputSlotStack == null) {
						status = MachineStatus.IDLE;
					} else {
						status = MachineStatus.ACTIVE;
					}
				}
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

			if (status != previousStatus) {
				if (status != MachineStatus.ACTIVE) {
					energyRate = 0;
				}
				setActiveStatus();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				markDirty();
			}

			inventory.flush();
		}
	}

	@Override
	public void randomDisplayTick(final World world, final int x, final int y,
			final int z, final Random rand) {
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
		return context != null
				&& context.recipeData.getMinimumInputQuantityRequired() <= activeStack.stackSize;
	}

	protected boolean flushBuffer() {

		if (buffer == null) {
			return true;
		}

		inventory.coeleceOutput();

		boolean isEmpty = true;

		for (int i = 0; i < buffer.size(); i++) {
			final ItemStack stack = buffer.get(i);
			if (stack != null) {
				if (inventory.addStackToOutput(stack)) {
					buffer.set(i, null);
				} else {
					isEmpty = false;
				}
			}
		}

		if (isEmpty) {
			buffer = null;
		}

		return isEmpty;
	}

	protected boolean recycleItem() {

		// Get how many items we need to snag off the stack
		// and remove them.
		decrStackSize(INPUT,
				context.recipeData.getMinimumInputQuantityRequired());

		// The necessary information should be in the context already.
		buffer = context.scrap();

		// Flush the generated stacks into the output buffer
		return flushBuffer();
	}

	@Override
	public void flush() {
		inventory.flush();
	}
}

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

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.client.ParticleEffects;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ComposterContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ComposterGui;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.MachineStatus;

import cofh.api.fluid.ITankContainerBucketable;

public class ComposterTileEntity extends TileEntityBase implements
		ITankContainerBucketable, IJobProgress, IFluidTank {

	public static final int UPDATE_ACTION_PROGRESS = 0;
	public static final int UPDATE_ACTION_STATUS = 1;
	public static final int UPDATE_WATER_LEVEL = 2;

	public static final int BROWN = 0;
	public static final int GREEN1 = 1;
	public static final int GREEN2 = 2;
	public static final int MEAL = 3;

	// Slot geometry for the machine
	public static final int[] BROWN_INPUT = { BROWN };
	public static final int[] GREEN_INPUT = { GREEN1, GREEN2 };
	public static final int[] OUTPUT_HOLD = { MEAL };
	public static final int[] ALL_SLOTS = { BROWN, GREEN1, GREEN2, MEAL };

	// State
	protected int waterLevel = 0;
	protected int progress = 0;
	protected MachineStatus status = MachineStatus.IDLE;
	int scanTickCount = 0;
	int scanIndex = 0;

	static final int WATER_MAX_STORAGE = 4000;
	static final int WATER_MAX_RECEIVE = 100;
	static final int COMPLETION_THRESHOLD = 100;
	static final int PROGRESS_DAYLIGHT_TICK = 2;
	static final int PROGRESS_NIGHTTIME_TICK = 1;
	static final int WATER_CONSUMPTION_DAYLIGHT_TICK = 2;
	static final int WATER_CONSUMPTION_NIGHTTIME_TICK = 1;
	static final int RAIN_GATHER_TICK = 4;

	static final int PLOT_SCAN_TICK_INTERVAL = 2;
	static final int PLOT_SIZE = 9;
	static final int PLOT_AREA = PLOT_SIZE * PLOT_SIZE;

	public ComposterTileEntity() {
		super(GuiIdentifier.COMPOSTER);
		SidedInventoryComponent inv = new SidedInventoryComponent(this,
				ALL_SLOTS.length);
		inv.setInputRange(0, ALL_SLOTS.length - 1);
		setMachineInventory(inv);

	}

	@Override
	public boolean isWhitelisted(ItemStack stack) {
		return true;
	}

	// /////////////////////////////////////
	//
	// Synchronization logic across client/server
	//
	// /////////////////////////////////////

	@Override
	public boolean receiveClientEvent(int action, int param) {

		if (!worldObj.isRemote)
			return true;

		switch (action) {
		case UPDATE_ACTION_PROGRESS:
			progress = param;
			break;
		case UPDATE_ACTION_STATUS:
			status = MachineStatus.map(param);
			break;
		case UPDATE_WATER_LEVEL:
			waterLevel = param;
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
		return (progress * 100) / COMPLETION_THRESHOLD;
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

		scanIndex = nbt.getByte("scanIndex");
		scanTickCount = nbt.getByte("scanTickCount");
		progress = nbt.getShort("progress");
		status = MachineStatus.map(nbt.getShort("status"));
		waterLevel = nbt.getShort("waterLevel");

		if (waterLevel > WATER_MAX_STORAGE)
			waterLevel = WATER_MAX_STORAGE;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setByte("scanIndex", (byte) scanIndex);
		nbt.setByte("scanTickCount", (byte) scanTickCount);
		nbt.setShort("progress", (short) progress);
		nbt.setShort("status", (short) status.ordinal());
		nbt.setShort("waterLevel", (short) waterLevel);
	}

	// /////////////////////////////////////
	//
	// TileEntityBase
	//
	// /////////////////////////////////////

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null || stack.stackSize == 0)
			return false;

		ItemScrapData data = ItemScrapData.get(stack);
		if (data == null)
			return false;

		CompostIngredient ci = data.getCompostIngredientValue();

		return (ci == CompostIngredient.BROWN && slot == BROWN)
				|| (ci == CompostIngredient.GREEN && (slot == GREEN1 || slot == GREEN2));
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public Object getGuiClient(InventoryPlayer inventory) {
		return new ComposterGui(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {
		return new ComposterContainer(inventory, this);
	}

	boolean hasGreenBrown() {
		ItemStack brown = getStackInSlot(BROWN);
		if (brown == null)
			return false;

		ItemStack green1 = getStackInSlot(GREEN1);
		ItemStack green2 = getStackInSlot(GREEN2);

		// This would only be true if bother are null
		if (green1 == green2)
			return false;

		if (green1 != null && green1.stackSize > 1)
			return true;

		if (green2 != null && green2.stackSize > 1)
			return true;

		// If we get here we have two green stacks
		// ergo 2 green ingredients
		return true;
	}

	int getEffectiveWaterUseThisTick() {
		return isDaylight() ? WATER_CONSUMPTION_DAYLIGHT_TICK
				: WATER_CONSUMPTION_NIGHTTIME_TICK;
	}

	boolean hasWater() {
		return waterLevel >= getEffectiveWaterUseThisTick();
	}

	boolean hasOutputRoom() {
		ItemStack output = getStackInSlot(MEAL);
		return output == null || output.stackSize < output.getMaxStackSize();
	}

	boolean canSeeSky() {
		return !worldObj.provider.hasNoSky
				&& worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord);
	}

	boolean isDaylight() {
		return worldObj.isDaytime();
	}

	boolean isRaining() {
		return worldObj.isRaining();
	}

	int getEffectiveProgressThisTick() {
		return isDaylight() ? PROGRESS_DAYLIGHT_TICK : PROGRESS_NIGHTTIME_TICK;
	}

	boolean canProceed() {
		return canSeeSky() && hasOutputRoom() && hasWater() && hasGreenBrown();
	}

	@Override
	public void updateEntity() {

		if (!worldObj.isRemote) {

			switch (status) {
			case IDLE:
				progress = 0;
				if (!canSeeSky())
					status = MachineStatus.NEED_MORE_RESOURCES;
				else if (hasOutputRoom() && hasGreenBrown())
					status = MachineStatus.ACTIVE;
				break;

			case ACTIVE:
				if (!hasWater()) {
					status = MachineStatus.OUT_OF_POWER;
				} else if (!canSeeSky()) {
					status = MachineStatus.NEED_MORE_RESOURCES;
				} else if (!hasOutputRoom() || !hasGreenBrown()) {
					status = MachineStatus.IDLE;
				} else {

					if (progress >= COMPLETION_THRESHOLD) {

						if (hasOutputRoom())
							handleCompletion();
					} else {
						progress += getEffectiveProgressThisTick();
						waterLevel -= getEffectiveWaterUseThisTick();
					}

					setMachineActive(true);
				}
				break;

			case NEED_MORE_RESOURCES:
				if (canSeeSky())
					if (hasOutputRoom() && hasGreenBrown())
						status = MachineStatus.ACTIVE;
					else
						status = MachineStatus.IDLE;
				break;

			case OUT_OF_POWER:
				if (!hasGreenBrown() || !hasOutputRoom())
					status = MachineStatus.IDLE;
				else if (hasWater())
					status = MachineStatus.ACTIVE;
				else if (!canSeeSky())
					status = MachineStatus.NEED_MORE_RESOURCES;
				break;

			default:
				break;
			}

			if (status != MachineStatus.ACTIVE) {
				setMachineActive(false);
			}

			if (isRaining()) {
				waterLevel += RAIN_GATHER_TICK;
				if (waterLevel > WATER_MAX_STORAGE)
					waterLevel = WATER_MAX_STORAGE;
			}

			doPlotScan();
		}
	}

	void handleCompletion() {

		ItemStack scratch = decrStackSize(BROWN, 1);
		scratch = decrStackSize(GREEN1, 2);
		if (scratch == null)
			scratch = decrStackSize(GREEN2, 2);
		else if (scratch.stackSize == 1)
			scratch = decrStackSize(GREEN2, 1);

		ItemStack output = getStackInSlot(MEAL);
		if (output == null)
			setInventorySlotContents(MEAL, new ItemStack(Items.dye, 1, 15));
		else
			output.stackSize++;

		markDirty();
		progress = 0;
	}

	void doPlotScan() {

		ItemStack meal = getStackInSlot(MEAL);
		if (meal == null)
			return;

		if ((++scanTickCount % PLOT_SCAN_TICK_INTERVAL) == 0) {
			scanTickCount = 0;

			int PLOT_ANCHOR_X = xCoord - PLOT_SIZE / 2;
			int PLOT_ANCHOR_Z = zCoord - PLOT_SIZE / 2;

			int blockX = (scanIndex % PLOT_SIZE) + PLOT_ANCHOR_X;
			int blockZ = (scanIndex / PLOT_SIZE) + PLOT_ANCHOR_Z;

			Block target = worldObj.getBlock(blockX, yCoord, blockZ);

			if (target != null && target instanceof IGrowable) {

				EntityPlayer recycling = ThermalRecycling
						.proxy()
						.getThermalRecyclingPlayer((WorldServer) worldObj,
								blockX, yCoord, blockZ).get();
				boolean applied = ItemDye.applyBonemeal(meal, worldObj, blockX, yCoord, blockZ,
						recycling);

				if(applied)
					ParticleEffects.bonemeal(worldObj, blockX, yCoord, blockZ, null);
				
				if (meal.stackSize == 0)
					setInventorySlotContents(MEAL, null);
			}

			if (++scanIndex == PLOT_AREA)
				scanIndex = 0;
		}
	}

	// //////////////////////////////////////
	//
	// ITankContainerBucketable
	//
	// //////////////////////////////////////

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { getInfo() };
	}

	@Override
	public boolean allowBucketDrain(ItemStack arg0) {
		return true;
	}

	@Override
	public boolean allowBucketFill(ItemStack arg0) {
		return true;
	}

	// /////////////////////////////////
	//
	// IFluidTank
	//
	// /////////////////////////////////

	@Override
	public FluidStack getFluid() {
		return FluidRegistry.getFluidStack("water", waterLevel);
	}

	@Override
	public int getFluidAmount() {
		return waterLevel;
	}

	@Override
	public int getCapacity() {
		return WATER_MAX_STORAGE;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {

		int result = Math.min(resource.amount, WATER_MAX_RECEIVE);
		result = Math.min(result, WATER_MAX_STORAGE - waterLevel);

		if (doFill) {
			waterLevel += result;
		}

		return result;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {

		int amountToDrain = Math.min(waterLevel, maxDrain);
		if (doDrain) {
			waterLevel -= amountToDrain;
		}
		return FluidRegistry.getFluidStack("water", amountToDrain);
	}
}

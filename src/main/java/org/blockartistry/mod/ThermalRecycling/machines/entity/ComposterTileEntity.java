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

import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.client.ParticleEffects;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ComposterContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.ComposterGui;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.gui.MachineStatus;
import org.blockartistry.mod.ThermalRecycling.util.FakePlayerHelper;
import org.blockartistry.mod.ThermalRecycling.util.FluidStackHelper;
import com.google.common.collect.ImmutableSet;

public final class ComposterTileEntity extends TileEntityBase implements
		IJobProgress, IMachineFluidHandler {

	private static final Set<IGrowable> bonemealBlackList = ImmutableSet.copyOf(new IGrowable[] { Blocks.grass,
			Blocks.double_plant, Blocks.tallgrass });

	public static final int UPDATE_ACTION_PROGRESS = 10;
	public static final int UPDATE_WATER_LEVEL = 11;

	public static final int BROWN = 0;
	public static final int GREEN1 = 1;
	public static final int GREEN2 = 2;
	public static final int MEAL = 3;

	// Slot geometry for the machine
	public static final int[] BROWN_INPUT = { BROWN };
	public static final int[] GREEN_INPUT = { GREEN1, GREEN2 };
	public static final int[] OUTPUT_HOLD = { MEAL };
	public static final int[] ALL_SLOTS = { BROWN, GREEN1, GREEN2, MEAL };

	private static final int WATER_MAX_STORAGE = 4000;
	private static final int COMPLETION_THRESHOLD = 1020;
	private static final int PROGRESS_DAYLIGHT_TICK = 30;
	private static final int PROGRESS_NIGHTTIME_TICK = 15;
	private static final int WATER_CONSUMPTION_DAYLIGHT_TICK = 2;
	private static final int WATER_CONSUMPTION_NIGHTTIME_TICK = 1;
	private static final int RAIN_GATHER_TICK = 3;

	private static final int PLOT_SCAN_TICK_INTERVAL = 2;
	private static final int PLOT_SIZE = 9;
	private static final int PLOT_AREA = PLOT_SIZE * PLOT_SIZE;
	
	private class NBT {
		public static final String SCAN_INDEX = "scanIndex";
		public static final String SCAN_TICK_COUNT = "scanTickCount";
		public static final String PROGRESS = "progress";
	}

	// State
	final private FluidTankComponent fluidTank;
	private int progress = 0;
	private int scanTickCount = 0;
	private int scanIndex = 0;
	
	// Non-persisted state
	private BiomeGenBase myBiome = null;

	public ComposterTileEntity() {
		super(GuiIdentifier.COMPOSTER);
		final SidedInventoryComponent inv = new SidedInventoryComponent(this,
				ALL_SLOTS.length);
		inv.setInputRange(0, ALL_SLOTS.length - 1);
		setMachineInventory(inv);

		fluidTank = new FluidTankComponent(new FluidStack(
				FluidStackHelper.FLUID_WATER, 0), WATER_MAX_STORAGE);
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
		case UPDATE_ACTION_PROGRESS:
			progress = param;
			break;
		case UPDATE_WATER_LEVEL:
			fluidTank.getFluid().amount = param;
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
	// /////////////////////////////////////

	@Override
	public int getPercentComplete() {
		return (progress * 100) / COMPLETION_THRESHOLD;
	}

	// /////////////////////////////////////
	//
	// INBTSerializer
	//
	// /////////////////////////////////////

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		scanIndex = nbt.getByte(NBT.SCAN_INDEX);
		scanTickCount = nbt.getByte(NBT.SCAN_TICK_COUNT);
		progress = nbt.getShort(NBT.PROGRESS);
		fluidTank.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setByte(NBT.SCAN_INDEX, (byte) scanIndex);
		nbt.setByte(NBT.SCAN_TICK_COUNT, (byte) scanTickCount);
		nbt.setShort(NBT.PROGRESS, (short) progress);
		fluidTank.writeToNBT(nbt);
	}

	// /////////////////////////////////////
	//
	// TileEntityBase
	//
	// /////////////////////////////////////

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		
		if(slot == MEAL)
			return false;

		final CompostIngredient ci = ItemData.get(stack).getCompostIngredientValue();

		return ci == CompostIngredient.BROWN && slot == BROWN
				|| ci == CompostIngredient.GREEN && (slot == GREEN1 || slot == GREEN2);
	}

	@Override
	public Object getGuiClient(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new ComposterGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new ComposterContainer(inventory, this);
	}

	boolean hasGreenBrown() {
		final ItemStack brown = inventory.getStackInSlot(BROWN);
		if (brown == null)
			return false;

		final ItemStack green1 = inventory.getStackInSlot(GREEN1);
		final ItemStack green2 = inventory.getStackInSlot(GREEN2);

		// This would only be true if both are null
		if (green1 == green2)
			return false;

		if (green1 != null && green2 == null && green1.stackSize < 2)
			return false;

		if (green2 != null && green1 == null && green2.stackSize < 2)
			return false;

		return true;
	}

	int getEffectiveWaterUseThisTick() {
		return isDaylight() ? WATER_CONSUMPTION_DAYLIGHT_TICK
				: WATER_CONSUMPTION_NIGHTTIME_TICK;
	}

	boolean hasWater() {
		return fluidTank.getFluidAmount() >= getEffectiveWaterUseThisTick();
	}

	boolean hasOutputRoom() {
		final ItemStack output = inventory.getStackInSlot(MEAL);
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
	
	boolean biomeHasRain() {
		if(myBiome == null) {
			myBiome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		}
		return !(myBiome.getEnableSnow() || myBiome.rainfall == 0);
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
			
			final MachineStatus previousStatus = status;

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
						fluidTank.drain(getEffectiveWaterUseThisTick(), true);
					}
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

			// Toggle the glow based on status
			if(previousStatus != status) {
				setActiveStatus();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			
			if (isRaining() && biomeHasRain())
				fluidTank.fill(new FluidStack(FluidStackHelper.FLUID_WATER,
						RAIN_GATHER_TICK), true);

			// If sky isn't blocked do the scan
			if(status != MachineStatus.NEED_MORE_RESOURCES)
				doPlotScan();
			
			inventory.flush();
		}
	}

	void handleCompletion() {

		ItemStack scratch = inventory.decrStackSize(BROWN, 1);
		scratch = inventory.decrStackSize(GREEN1, 2);
		if (scratch == null) {
			scratch = inventory.decrStackSize(GREEN2, 2);
		} else if (scratch.stackSize == 1) {
			scratch = inventory.decrStackSize(GREEN2, 1);
		}

		final ItemStack output = inventory.getStackInSlot(MEAL);
		if (output == null) {
			inventory.setInventorySlotContents(MEAL, new ItemStack(Items.dye, 1, 15));
		} else {
			output.stackSize++;
			inventory.setInventorySlotContents(MEAL, output);
		}

		progress = 0;
	}

	boolean bonemealTarget(final Block block) {
		if (block == null || !(block instanceof IGrowable))
			return false;
		
		return !bonemealBlackList.contains(block);
	}

	void doPlotScan() {

		final ItemStack meal = inventory.getStackInSlot(MEAL);
		if (meal == null)
			return;

		if (++scanTickCount % PLOT_SCAN_TICK_INTERVAL == 0) {
			scanTickCount = 0;

			final int PLOT_ANCHOR_X = xCoord - PLOT_SIZE / 2;
			final int PLOT_ANCHOR_Z = zCoord - PLOT_SIZE / 2;

			final int blockX = scanIndex % PLOT_SIZE + PLOT_ANCHOR_X;
			final int blockZ = scanIndex / PLOT_SIZE + PLOT_ANCHOR_Z;

			final Block target = worldObj.getBlock(blockX, yCoord, blockZ);

			if (bonemealTarget(target)) {

				final EntityPlayer recycling = FakePlayerHelper.getFakePlayer((WorldServer) worldObj,
								blockX, yCoord, blockZ).get();
				final boolean applied = ItemDye.applyBonemeal(meal, worldObj, blockX,
						yCoord, blockZ, recycling);

				if (applied)
					ParticleEffects.bonemeal(worldObj, blockX, yCoord, blockZ,
							null);

				if (meal.stackSize == 0)
					inventory.setInventorySlotContents(MEAL, null);
				else
					inventory.setInventorySlotContents(MEAL, meal);
			}

			if (++scanIndex == PLOT_AREA)
				scanIndex = 0;
		}
	}

	@Override
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
		if (!ModOptions.getEnableComposterFX())
			return;

		if (status != MachineStatus.OUT_OF_POWER || rand.nextInt(9) != 0)
			return;

		ParticleEffects.spawnParticlesAroundBlock("splash", world, x, y, z,
				rand);
	}

	public IFluidTank getFluidTank() {
		return fluidTank;
	}

	@Override
	public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
		return fluidTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final FluidStack resource,
			final boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(fluidTank.getFluid())) {
			return null;
		}
		return fluidTank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
		return fluidTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(final ForgeDirection from, final Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
		return new FluidTankInfo[] { fluidTank.getInfo() };
	}
	
	@Override
	public void flush() {
		inventory.flush();
	}
}

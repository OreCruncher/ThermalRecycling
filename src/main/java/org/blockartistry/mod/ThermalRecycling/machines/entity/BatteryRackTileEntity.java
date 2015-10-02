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

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.machines.gui.BatteryRackContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.BatteryRackGui;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import cpw.mods.fml.common.Optional;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.InterfaceList(value = {
		@Optional.Interface(iface = "cofh.api.energy.IEnergyProvider", modid = "CoFHCore", striprefs = true),
		@Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHCore", striprefs = true), })
public final class BatteryRackTileEntity extends TileEntityBase implements IEnergyProvider, IEnergyInfo {

	// Slot geometry for the machine
	public static final int INPUT = 0;

	// Update actions
	public static final int UPDATE_ACTION_ENERGY_RATE = 12;

	private class NBT {
		public static final String ENERGY_RATE = "energyRate";
	}

	private static final int ENERGY_MAX_TRANSFER = ModOptions.getBatteryRackTransfer();

	// Persistent state
	protected int energyRate;

	// Transient state
	protected ItemStack input;
	protected IEnergyContainerItem energyContainer;
	protected List<EnergyConnection> connections;

	public BatteryRackTileEntity() {
		super(GuiIdentifier.BATTERY_RACK);
		final SidedInventoryComponent inv = new SidedInventoryComponent(this, 11);
		inv.setInputRange(0, 1);
		setMachineInventory(inv);
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return slot == INPUT && stack.getItem() instanceof IEnergyContainerItem;
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
		case UPDATE_ACTION_ENERGY_RATE:
			energyRate = param;
			break;
		default:
			return super.receiveClientEvent(action, param);
		}

		return true;
	}

	// /////////////////////////////////////
	//
	// INBTSerializer
	//
	// /////////////////////////////////////

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		energyRate = nbt.getShort(NBT.ENERGY_RATE);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setShort(NBT.ENERGY_RATE, (short) energyRate);
	}

	// /////////////////////////////////////
	//
	// IEnergyProvider
	//
	// /////////////////////////////////////

	@Override
	public boolean canConnectEnergy(final ForgeDirection from) {
		return from == ForgeDirection.UP || from == ForgeDirection.DOWN
				|| from == ForgeDirection.getOrientation(getFacing()).getOpposite();
	}

	@Override
	public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
		detectInputStack();
		return energyContainer == null ? 0
				: energyContainer.extractEnergy(input, Math.min(maxExtract, ENERGY_MAX_TRANSFER), simulate);
	}

	@Override
	public int getEnergyStored(final ForgeDirection from) {
		detectInputStack();
		return energyContainer == null ? 0 : energyContainer.getEnergyStored(input);
	}

	@Override
	public int getMaxEnergyStored(final ForgeDirection from) {
		detectInputStack();
		return energyContainer == null ? 0 : energyContainer.getMaxEnergyStored(input);
	}

	// /////////////////////////////////////
	//
	// IEnergyInfo
	//
	// /////////////////////////////////////

	@Override
	public int getInfoEnergyPerTick() {
		return energyRate;
	}

	@Override
	public int getInfoEnergyStored() {
		return getEnergyStored(ForgeDirection.UNKNOWN);
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return extractEnergy(ForgeDirection.UNKNOWN, ENERGY_MAX_TRANSFER, true);
	}

	@Override
	public int getInfoMaxEnergyStored() {
		return getMaxEnergyStored(ForgeDirection.UNKNOWN);
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
	public Object getGuiClient(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new BatteryRackGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final GuiIdentifier id, final InventoryPlayer inventory) {
		return new BatteryRackContainer(inventory, this);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		// If a neighbor changed clear the list so that it is
		// regenerated during the update.
		connections = null;
	}

	@Override
	public void updateEntity() {

		if (worldObj.isRemote)
			return;

		detectInputStack();

		final MachineStatus previousStatus = status;

		if (input != null) {

			// Get the possible amount to send to the receivers. This is the
			// amount
			// of RF TOTAL per tick that the rack can send.
			int sendAmount = energyContainer.extractEnergy(input, ENERGY_MAX_TRANSFER, true);
			if (sendAmount == 0) {
				status = MachineStatus.IDLE;
			} else {

				if (connections == null)
					connections = getTargetList();

				status = MachineStatus.ACTIVE;

				// Get a list of all potential receivers. The list is in
				// preferred
				// order
				// of handling.
				if (!connections.isEmpty()) {
					// We have power, and we have targets. Loop through
					// parceling the
					// energy.
					energyRate = 0;
					for (final EnergyConnection receiver : connections) {
						final int receiveAmount = receiver.receiveEnergy(sendAmount, true);
						if (receiveAmount != 0) {
							energyContainer.extractEnergy(input, receiveAmount, false);
							receiver.receiveEnergy(receiveAmount, false);
							sendAmount -= receiveAmount;
							energyRate += receiveAmount;

							// If we run out of power there isn't much to do.
							if (sendAmount == 0) {
								break;
							}
						}
					}
				}
			}
		} else {
			status = MachineStatus.IDLE;
		}

		// IDLE means no energy available
		// ACTIVE means energy available, not necessarily providing it
		if (status != previousStatus) {
			setActiveStatus();
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
		}
		
		flush();

	}

	@Override
	public void flush() {
		inventory.flush();
	}

	// Miscellaneous
	protected void detectInputStack() {
		final ItemStack stack = inventory.getStackInSlot(INPUT);
		if (stack == input)
			return;

		if (stack != null && stack.getItem() instanceof IEnergyContainerItem) {
			input = stack;
			energyContainer = (IEnergyContainerItem) stack.getItem();
		} else {
			input = null;
			energyContainer = null;
		}
	}

	protected static final class EnergyConnection {

		private final ForgeDirection direction;
		private final IEnergyReceiver receiver;

		public EnergyConnection(final ForgeDirection dir, final IEnergyReceiver rec) {
			direction = dir;
			receiver = rec;
		}

		public int receiveEnergy(final int amount, final boolean simulate) {
			return receiver.receiveEnergy(direction, amount, simulate);
		}
	}

	protected IEnergyReceiver getTargetReceiver(final ForgeDirection direction) {
		final TileEntity te = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY,
				zCoord + direction.offsetZ);
		return (te instanceof IEnergyReceiver) ? (IEnergyReceiver) te : null;
	}

	protected List<EnergyConnection> getTargetList() {
		final List<EnergyConnection> targets = new ArrayList<EnergyConnection>();

		final ForgeDirection myOrientation = ForgeDirection.getOrientation(getFacing());
		final IEnergyReceiver top = getTargetReceiver(ForgeDirection.UP);
		final IEnergyReceiver bottom = getTargetReceiver(ForgeDirection.DOWN);
		final IEnergyReceiver back = getTargetReceiver(myOrientation.getOpposite());

		if (back != null)
			targets.add(new EnergyConnection(myOrientation, back));

		if (top != null)
			targets.add(new EnergyConnection(ForgeDirection.DOWN, top));

		if (bottom != null)
			targets.add(new EnergyConnection(ForgeDirection.UP, bottom));

		return targets;
	}
}

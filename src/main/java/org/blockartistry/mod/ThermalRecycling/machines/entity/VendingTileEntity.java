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

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;
import org.blockartistry.mod.ThermalRecycling.machines.gui.VendingContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.VendingGui;
import org.blockartistry.mod.ThermalRecycling.machines.gui.VendingOwnerContainer;
import org.blockartistry.mod.ThermalRecycling.machines.gui.VendingOwnerGui;

public class VendingTileEntity extends TileEntityBase {

	// The empty UUID is "zero". It is used to identify a TE that has
	// no owner, which usually occurs when the Vending Machine is
	// placed.
	public static final UUID NO_OWNER = new UUID(0, 0);

	// Name to display if the vending machine is an admin
	// shop
	private static final String adminVendingName = StatCollector
			.translateToLocal("msg.MachineVending.adminName");
	
	private static final boolean BLOCK_PIPE_CONNECTION = ModOptions.getVendingDisallowPipeConnection();

	// Slot geometry - based on hardened strongbox storage
	public static final int GENERAL_INVENTORY_SIZE = 9 * 3;
	public static final int CONFIG_INVENTORY_SIZE = 3 * 6;
	public static final int TOTAL_INVENTORY_SIZE = GENERAL_INVENTORY_SIZE
			+ CONFIG_INVENTORY_SIZE;
	public static final int INVENTORY_SLOT_START = 0;
	public static final int CONFIG_SLOT_START = GENERAL_INVENTORY_SIZE;

	private final class NBT {
		public final static String OWNER = "owner";
		public final static String OWNER_NAME = "ownerName";
		public final static String ADMIN_MODE = "admin";
	}

	// Persisted state
	private UUID ownerId = NO_OWNER;
	private String ownerName = "";
	private boolean adminMode = false;

	public VendingTileEntity() {
		super(GuiIdentifier.VENDING);

		// The inventory is large enough to hold both the actual
		// vending inventory as well as the configuration slots.
		// Because the inventory is like a normal chest the input
		// and output ranges overlap.
		final SidedInventoryComponent inv = new SidedInventoryComponent(this,
				TOTAL_INVENTORY_SIZE);
		inv.setInputRange(INVENTORY_SLOT_START, GENERAL_INVENTORY_SIZE);
		inv.setOutputRange(INVENTORY_SLOT_START, GENERAL_INVENTORY_SIZE);
		setMachineInventory(inv);
	}

	// Called by break event handler to see if it is OK to break
	// the block based on ownership.
	public boolean okToBreak(final EntityPlayer player) {
		return ownerId.compareTo(NO_OWNER) == 0
				|| ownerId.compareTo(player.getPersistentID()) == 0;
	}

	public String getOwnerName() {
		return adminMode ? adminVendingName : ownerName;
	}

	public boolean isAdminMode() {
		return adminMode;
	}

	/**
	 * Indicates if the machine can be locked. The notion of locked is up to the
	 * implementation.
	 */
	public boolean isLockable(final EntityPlayer player) {
		return player.capabilities.isCreativeMode;
	}

	/**
	 * Toggles the lock status of the machine.
	 */
	public boolean toggleLock() {
		adminMode = !adminMode;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
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

		if (nbt.hasKey(NBT.OWNER)) {
			ownerId = UUID.fromString(nbt.getString(NBT.OWNER));
			ownerName = nbt.getString(NBT.OWNER_NAME);
			adminMode = nbt.getBoolean(NBT.ADMIN_MODE);
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString(NBT.OWNER, ownerId.toString());
		nbt.setString(NBT.OWNER_NAME, ownerName);
		nbt.setBoolean(NBT.ADMIN_MODE, adminMode);
	}

	// /////////////////////////////////////
	//
	// TileEntityBase
	//
	// /////////////////////////////////////

	@Override
	public boolean onBlockActivated(final World world, final int x,
			final int y, final int z, final EntityPlayer player,
			final int side, final float a, final float b, final float c) {

		if (!world.isRemote) {

			final boolean isOpped = player.capabilities.isCreativeMode;
			boolean isOwner = false;
			GuiIdentifier theGui = myGui;

			// See if the owner needs to be set. The first one to access the
			// device after placement becomes the owner.
			if (ownerId.compareTo(NO_OWNER) == 0) {
				ownerId = player.getPersistentID();
				ownerName = player.getDisplayName();
				isOwner = true;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				markDirty();
			} else {
				isOwner = ownerId.compareTo(player.getPersistentID()) == 0;

				// If this is the owner keep the owner name in sync
				if (isOwner && !ownerName.equals(player.getDisplayName())) {
					ownerName = player.getDisplayName();
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					markDirty();
				}
			}

			// If the owner is opening without sneaking then go to
			// the storage GUI. Otherwise the trade GUI will be opened.
			if ((isOwner || isOpped) && !player.isSneaking()) {
				theGui = GuiIdentifier.VENDING_OWNER;
			}

			player.openGui(ThermalRecycling.MOD_ID, theGui.ordinal(), world, x,
					y, z);
		}

		return true;
	}

	@Override
	public Object getGuiClient(final GuiIdentifier id,
			final InventoryPlayer inventory) {
		if (id == GuiIdentifier.VENDING_OWNER)
			return new VendingOwnerGui(inventory, this);
		return new VendingGui(inventory, this);
	}

	@Override
	public Object getGuiServer(final GuiIdentifier id,
			final InventoryPlayer inventory) {
		if (id == GuiIdentifier.VENDING_OWNER)
			return new VendingOwnerContainer(inventory, this);
		return new VendingContainer(inventory, this);
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return slot < CONFIG_SLOT_START;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(final int side) {
		return BLOCK_PIPE_CONNECTION ? new int[0] : inventory.getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack stack, final int facing) {
		return BLOCK_PIPE_CONNECTION ? false : inventory.canInsertItem(slot, stack, facing);
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack stack, final int facing) {
		return BLOCK_PIPE_CONNECTION ? false : inventory.canExtractItem(slot, stack, facing);
	}

	@Override
	public void flush() {
		inventory.flush();
	}
}

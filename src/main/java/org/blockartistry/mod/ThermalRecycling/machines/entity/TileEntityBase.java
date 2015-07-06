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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.MachineBase;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiIdentifier;

import cofh.api.tileentity.IReconfigurableFacing;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.Interface(iface="cofh.api.tileentity.IReconfigurableFacing", modid="CoFHCore", striprefs=true)
public abstract class TileEntityBase extends TileEntity implements
		IMachineInventory, IReconfigurableFacing  {

	private static final int[] emptyList = new int[0];

	// Reserve 0-9 for TileEntityBase
	public static final int UPDATE_ACTION_STATUS = 0;
	
	private static class NBT {
		public static final String STATUS = "status";
	};
	
	protected IMachineInventory inventory = new NoInventoryComponent();
	protected GuiIdentifier myGui;
	
	// Part of the TileEntity state
	protected MachineStatus status = MachineStatus.IDLE;

	public TileEntityBase(final GuiIdentifier gui) {
		myGui = gui;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		// Initializing to a base state from the server
		readFromNBT(pkt.func_148857_g());
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		// Sends out the base state to clients
		final NBTTagCompound syncData = new NBTTagCompound();
		writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, syncData);
	}

	public boolean dropInventoryWhenBroke() {
		return true;
	}

	public boolean allowPipeConnection() {
		return true;
	}
	
	/*
	 * Called when a status update occurs.  Sub-class should process actions
	 * first before delegating to this implementation.
	 */
	@Override
	public boolean receiveClientEvent(final int action, final int param) {

		if (!worldObj.isRemote)
			return true;

		switch (action) {
		case UPDATE_ACTION_STATUS:
			status = MachineStatus.map(param);
			break;
		default:
			;
		}

		return true;
	}

	protected void setMachineInventory(final IMachineInventory inv) {
		inventory = inv;
		if (inventory == null)
			inventory = new NoInventoryComponent();
	}
	
	public IInventory getMachineInventory() {
		return inventory;
	}
	
	public void setActiveStatus() {
		final MachineStatus status = getStatus();
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		final int temp = meta;
		
		if(status == MachineStatus.ACTIVE)
			meta |= MachineBase.META_ACTIVE_LIGHT_BIT;
		else
			meta &= ~MachineBase.META_ACTIVE_LIGHT_BIT;
		
		if(meta != temp) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
		}
	}
	
	public boolean onBlockActivated(final World world, final int x, final int y, final int z,
			final EntityPlayer player, final int side, final float a, final float b, final float c) {

		if (!world.isRemote && !player.isSneaking()) {
			player.openGui(ThermalRecycling.MOD_ID, myGui.ordinal(), world, x,
					y, z);
		}

		return true;
	}

	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {

	}

	public Object getGuiClient(final GuiIdentifier id, final InventoryPlayer inventory) {
		return null;
	}

	public Object getGuiServer(final GuiIdentifier id, final InventoryPlayer inventory) {
		return null;
	}
	
	@Override
	public void dropInventory(final World world, final int x, final int y, final int z) {
		if(dropInventoryWhenBroke())
			inventory.dropInventory(world, x, y, z);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
		status = MachineStatus.map(nbt.getShort(NBT.STATUS));
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
		nbt.setShort(NBT.STATUS, (short) status.ordinal());
	}

	// /////////////////////////////////////
	//
	// ISidedInventory
	//
	// /////////////////////////////////////

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(final int index, final int count) {
		return inventory.decrStackSize(index, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int index) {
		return inventory.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack stack) {
		inventory.setInventorySlotContents(index, stack);
	}

	@Override
	public String getInventoryName() {
		return inventory.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return inventory.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		inventory.openInventory();
	}

	@Override
	public void closeInventory() {
		inventory.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int side) {
		return allowPipeConnection()  ? inventory.getAccessibleSlotsFromSide(side) : emptyList;
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack stack, final int facing) {
		return allowPipeConnection() ? inventory.canInsertItem(slot, stack, facing) : false;
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack stack, final int facing) {
		return allowPipeConnection() ? inventory.canExtractItem(slot, stack, facing) : false;
	}

	@Override
	public boolean addStackToOutput(final ItemStack stack) {
		return inventory.addStackToOutput(stack);
	}
	
	@Override
	public boolean removeStackFromOutput(final ItemStack stack) {
		return inventory.removeStackFromOutput(stack);
	}
	
	@Override
	public void coeleceOutput() {
		inventory.coeleceOutput();
	}

	@Override
	public boolean isStackAlreadyInSlot(final int slot, final ItemStack stack) {
		return inventory.isStackAlreadyInSlot(slot, stack);
	}
	
	@Override
	public ItemStack[] getRawInventory() {
		return inventory.getRawInventory();
	}

	/**
	 * Indicates if the machine can be locked.  The notion of locked is
	 * up to the implementation.
	 */
	public boolean isLockable(final EntityPlayer player) {
		return false;
	}
	
	/**
	 * Toggles the lock status of the machine.
	 */
	public boolean toggleLock() {
		return false;
	}
	
	public boolean isNameColorable(final EntityPlayer player) {
		return false;
	}
	
	public int getNameColor() {
		return 15;
	}
	
	public int getNameBackgroundColor() {
		return 0;
	}
	
	public void setNameColor(final int color) { }
	
	public void setNameBackgroundColor(final int color) { }

	// /////////////////////////////////////
	//
	// IReconfigurableFacing
	//
	// /////////////////////////////////////
	public int getFacing() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & MachineBase.META_SIDE_MASK;
	}

	public boolean allowYAxisFacing() {
		return false;
	}

	public boolean rotateBlock() {
		
		int facing = getFacing();
		
		// Check to see if the direction is valid.  If it isn't
		// make it valid.  This could happen when upgrading from
		// a pre v0.3.9.x build when rotation was put in.
		if(facing < 2 || facing > 5)
			facing = 2;
		
		final ForgeDirection newDirection = ForgeDirection.getOrientation(facing).getRotation(ForgeDirection.UP);
		if(newDirection != ForgeDirection.UNKNOWN)
			setFacing(newDirection.ordinal());
		return newDirection != ForgeDirection.UNKNOWN;
	}

	public boolean setFacing(final int i) {
		final boolean isLit = (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & MachineBase.META_ACTIVE_LIGHT_BIT) != 0;
		final int meta = i | (isLit ? MachineBase.META_ACTIVE_LIGHT_BIT : 0);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
		return true;
	}

	public MachineStatus getStatus() {
		return status;
	}
}

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
	
	protected IMachineInventory inventory = new NoInventoryComponent();
	protected GuiIdentifier myGui;

	public TileEntityBase(final GuiIdentifier gui) {
		myGui = gui;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		// Initializing to a base state from the server
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket() {
		// Sends out the base state to clients
		final NBTTagCompound syncData = new NBTTagCompound();
		writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, syncData);
	}

	protected void setMachineInventory(final IMachineInventory inv) {
		inventory = inv;
		if (inventory == null)
			inventory = new NoInventoryComponent();
	}
	
	public IInventory getMachineInventory() {
		return inventory;
	}

	// Toggles the meta data so that it is considered active.
	// This will result in the active face being displayed as well as
	// have a little bit of light.
	protected void setMachineActive(final boolean toggle) {

		if (!worldObj.isRemote) {

			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			final int temp = meta;

			if (toggle)
				meta |= MachineBase.META_ACTIVE_INDICATOR;
			else
				meta &= ~MachineBase.META_ACTIVE_INDICATOR;

			if (meta != temp)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
						meta, 2);
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
	
	public boolean isWhitelisted(final int slot, final ItemStack stack) {
		return true;
	}
	
	@Override
	public void dropInventory(final World world, final int x, final int y, final int z) {
		inventory.dropInventory(world, x, y, z);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
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
		//return inventory.isItemValidForSlot(slot, stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int side) {
		return inventory.getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack stack, final int facing) {
		return inventory.canInsertItem(slot, stack, facing);
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack stack, final int facing) {
		return inventory.canExtractItem(slot, stack, facing);
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
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & ~MachineBase.META_ACTIVE_INDICATOR;
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
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		meta = i | (meta & MachineBase.META_ACTIVE_INDICATOR);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
		return true;
	}
}

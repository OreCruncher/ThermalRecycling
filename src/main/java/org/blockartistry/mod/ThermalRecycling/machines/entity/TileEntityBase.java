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

import org.blockartistry.mod.ThermalRecycling.machines.MachineBase;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityBase extends TileEntity implements
		IMachineInventory {

	protected IMachineInventory inventory = new NoInventoryComponent();

	protected void setMachineInventory(IMachineInventory inv) {
		inventory = inv;
		if (inventory == null)
			inventory = new NoInventoryComponent();
	}

	// Toggles the thermalRecycler meta data so that it is considered active.
	// This will result in the active face being displayed as well as
	// have a little bit of light.
	protected void setMachineActive(boolean toggle) {

		if (!worldObj.isRemote) {

			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			int temp = meta;

			if (toggle)
				meta |= MachineBase.META_ACTIVE_INDICATOR;
			else
				meta &= ~MachineBase.META_ACTIVE_INDICATOR;

			if (meta != temp)
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
						meta, 2);
		}

	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float a, float b, float c) {

		return false;
	}

	public Object getGuiClient(InventoryPlayer inventory) {
		return null;
	}

	public Object getGuiServer(InventoryPlayer inventory) {
		return null;
	}

	public boolean isWhitelisted(ItemStack stack) {
		return true;
	}

	public void dropInventory(Block oldBlock, int oldMeta, int x, int y, int z) {

		if (this instanceof IInventory) {

			IInventory inv = this;

			Random rand = new Random();

			for (int i = 0; i < inv.getSizeInventory(); i++) {

				ItemStack stack = inv.getStackInSlot(i);

				if (stack != null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;

					while (stack.stackSize > 0) {
						int j = rand.nextInt(21) + 10;

						if (j > stack.stackSize) {
							j = stack.stackSize;
						}

						stack.stackSize -= j;

						EntityItem item = new EntityItem(worldObj, x + f, y
								+ f1, z + f2, new ItemStack(stack.getItem(), j,
								stack.getItemDamage()));

						if (stack.hasTagCompound()) {
							item.getEntityItem().setTagCompound(
									(NBTTagCompound) stack.getTagCompound()
											.copy());
						}

						worldObj.spawnEntityInWorld(item);
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
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
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventory.decrStackSize(index, count);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		return inventory.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
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
	public boolean isUseableByPlayer(EntityPlayer player) {
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return inventory.isItemValidForSlot(slot, stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return inventory.getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int facing) {
		return inventory.canInsertItem(slot, stack, facing);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int facing) {
		return inventory.canExtractItem(slot, stack, facing);
	}

	@Override
	public boolean addStackToOutput(ItemStack stack) {
		return inventory.addStackToOutput(stack);
	}

	/*
	 * public void sendDeltaUpdate(int action, int value) {
	 * if(!worldObj.isRemote) { this.worldObj.addBlockEvent(xCoord, yCoord,
	 * zCoord, getBlockType(), action, value); } }
	 */
}

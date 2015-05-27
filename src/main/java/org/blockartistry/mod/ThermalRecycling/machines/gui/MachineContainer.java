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

package org.blockartistry.mod.ThermalRecycling.machines.gui;

import org.blockartistry.mod.ThermalRecycling.machines.entity.TileEntityBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class MachineContainer<T extends TileEntityBase> extends Container {
	
	private static final int UPDATE_TICK_INTERVAL = 3;
	
	final T entity;
	final int sizeInventory;
	protected int spamCycle;

	public MachineContainer(final T entity) {
		this.entity = entity;
		this.sizeInventory = entity.getSizeInventory();
		this.spamCycle = 0;
	}
	
	protected void addPlayerInventory(final InventoryPlayer inv) {
		
		Slot s;
		
		// Add the player inventory
		for (int i = 0; i < 27; ++i) {

			// The constants are offsets from the left and top edge
			// of the GUI
			final int h = (i % 9) * 18 + 8;
			final int v = (i / 9) * 18 + 84;

			// We offset by 9 to skip the hotbar slots - they
			// come next
			s = new Slot(inv, i + 9, h, v);
			addSlotToContainer(s);
		}

		// Add the hotbar
		for (int i = 0; i < 9; ++i) {
			s = new Slot(inv, i, 8 + i * 18, 142);
			addSlotToContainer(s);
		}

	}

	/**
	 * Override to provide logic for handling machine specific status
	 * processing.  This will occur during detectAndSendChanges().
	 */
	public void handleStatus() { }
	
	// Rework the base slot change mechanism.  The focus of this
	// routine is to minimize creation of new ItemStacks and
	// associated copying.  Should increase performance and
	// ease GC pressure when running on servers.
	//
	// Note that the ItemStacks in Containers aren't really
	// transient.  They can be held for a period of time.  Keep
	// a machine GUI open and they can become long lived.
	//
	// The rationale for doing this is for machine slots.
	// During operations what changes the most about the stacks
	// are the quantities, and whether a stack disappears or
	// appears in a slot.  The routine below tries to keep
	// the allocated cached ItemStack and modify its properties
	// rather than doing a realloc and copy.
    @SuppressWarnings("unchecked")
    @Override
	public void detectAndSendChanges() {
    	
    	if(++spamCycle % UPDATE_TICK_INTERVAL != 0) {
    		return;
    	}
    	
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
        	
            final ItemStack slotItemStack = ((Slot)this.inventorySlots.get(i)).getStack();
            ItemStack cacheItemStack = (ItemStack)this.inventoryItemStacks.get(i);
            
            if(slotItemStack == null && cacheItemStack == null)
            	continue;
            
        	boolean areDifferent = false;
        	
        	// If either stack is null, or the item type changes, do an alloc and copy
            if(slotItemStack == null || cacheItemStack == null || slotItemStack.getItem() != cacheItemStack.getItem()) {
                cacheItemStack = slotItemStack == null ? null : slotItemStack.copy();
                areDifferent = true;
            } else {
            	
            	// The ItemStack is fundamentally the same.  Adjust the stack size,
            	// damage and NBT data as needed.
            	if(cacheItemStack.stackSize != slotItemStack.stackSize) {
            		cacheItemStack.stackSize = slotItemStack.stackSize;
            		areDifferent = true;
            	}
            	
            	if(cacheItemStack.getItemDamage() != slotItemStack.getItemDamage()) {
            		cacheItemStack.setItemDamage(slotItemStack.getItemDamage());
            		areDifferent = true;
            	}
            	
            	if(!ItemStack.areItemStackTagsEqual(cacheItemStack, slotItemStack)) {
            		final NBTTagCompound nbt = (NBTTagCompound) (slotItemStack.hasTagCompound() ? slotItemStack.getTagCompound().copy() : null);
            		cacheItemStack.setTagCompound(nbt);
            		areDifferent = true;
            	}
            }

            // Only process if there was a change to the slot
            if(areDifferent) {
	            this.inventoryItemStacks.set(i, cacheItemStack);
	
	            for (int j = 0; j < this.crafters.size(); ++j) {
	                ((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, cacheItemStack);
	            }
            }
        }
        
        handleStatus();
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(final int id, final int data) {
		entity.receiveClientEvent(id, data);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		return entity.isUseableByPlayer(playerIn);
	}
}

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
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class MachineContainer<T extends TileEntityBase> extends
		Container {

	protected static final int PLAYER_HOTBAR_SIZE = 9;
	protected static final int PLAYER_CHEST_SIZE = 27;
	protected static final int PLAYER_INVENTORY_SIZE = PLAYER_HOTBAR_SIZE
			+ PLAYER_CHEST_SIZE;
	protected static final int GUI_INVENTORY_CELL_SIZE = 18;

	private static final int UPDATE_TICK_INTERVAL = 3;

	protected final T entity;
	protected int spamCycle;

	public MachineContainer(final T entity) {
		this.entity = entity;
		this.spamCycle = 0;
	}

	protected void addPlayerInventory(final InventoryPlayer inv,
			final int guiHeight) {

		int yOffset = guiHeight - 82;

		// Add the player inventory
		for (int i = 0; i < PLAYER_CHEST_SIZE; ++i) {

			// The constants are offsets from the left and top edge
			// of the GUI
			final int h = (i % PLAYER_HOTBAR_SIZE) * GUI_INVENTORY_CELL_SIZE
					+ 8;
			final int v = (i / PLAYER_HOTBAR_SIZE) * GUI_INVENTORY_CELL_SIZE
					+ yOffset;

			// We offset by 9 to skip the hotbar slots - they
			// come next
			addSlotToContainer(new Slot(inv, i + 9, h, v));
		}

		yOffset = guiHeight - 24;
		// Add the hotbar
		for (int i = 0; i < PLAYER_HOTBAR_SIZE; ++i) {
			addSlotToContainer(new Slot(inv, i,
					8 + i * GUI_INVENTORY_CELL_SIZE, yOffset));
		}
	}

	/**
	 * Override to provide logic for handling machine specific status
	 * processing. This will occur during detectAndSendChanges().
	 */
	public void handleStatus() {
	}

	// Rework the base slot change mechanism. The focus of this
	// routine is to minimize creation of new ItemStacks and
	// associated copying. Should increase performance and
	// ease GC pressure when running on servers.
	//
	// Note that the ItemStacks in Containers aren't really
	// transient. They can be held for a period of time. Keep
	// a machine GUI open and they can become long lived.
	//
	// The rationale for doing this is for machine slots.
	// During operations what changes the most about the stacks
	// are the quantities, and whether a stack disappears or
	// appears in a slot. The routine below tries to keep
	// the allocated cached ItemStack and modify its properties
	// rather than doing a realloc and copy.
	@SuppressWarnings("unchecked")
	@Override
	public void detectAndSendChanges() {

		if (++spamCycle % UPDATE_TICK_INTERVAL != 0) {
			return;
		}

		final int invSize = this.inventorySlots.size();
		for (int i = 0; i < invSize; ++i) {

			final ItemStack slotItemStack = ((Slot) this.inventorySlots.get(i))
					.getStack();
			ItemStack cacheItemStack = (ItemStack) this.inventoryItemStacks
					.get(i);

			if (slotItemStack == null && cacheItemStack == null)
				continue;

			boolean areDifferent = false;

			// If either stack is null, or the item type changes, do an alloc
			// and copy
			if (slotItemStack == null || cacheItemStack == null
					|| slotItemStack.getItem() != cacheItemStack.getItem()) {
				cacheItemStack = slotItemStack == null ? null : slotItemStack
						.copy();
				areDifferent = true;
			} else {

				// The ItemStack is fundamentally the same. Adjust the stack
				// size,
				// damage and NBT data as needed.
				if (cacheItemStack.stackSize != slotItemStack.stackSize) {
					cacheItemStack.stackSize = slotItemStack.stackSize;
					areDifferent = true;
				}

				if (cacheItemStack.getItemDamage() != slotItemStack
						.getItemDamage()) {
					cacheItemStack.setItemDamage(slotItemStack.getItemDamage());
					areDifferent = true;
				}

				if (!ItemStack.areItemStackTagsEqual(cacheItemStack,
						slotItemStack)) {
					final NBTTagCompound nbt = (NBTTagCompound) (slotItemStack
							.hasTagCompound() ? slotItemStack.getTagCompound()
							.copy() : null);
					cacheItemStack.setTagCompound(nbt);
					areDifferent = true;
				}
			}

			// Only process if there was a change to the slot
			if (areDifferent) {
				this.inventoryItemStacks.set(i, cacheItemStack);

				final int craftersSize = this.crafters.size();
				for (int j = 0; j < craftersSize; ++j) {
					((ICrafting) this.crafters.get(j)).sendSlotContents(this,
							i, cacheItemStack);
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

	public boolean mergeToPlayerInventory(final ItemStack stack) {
		final int sizeInventory = entity.getSizeInventory();
		return mergeItemStack(stack, sizeInventory, sizeInventory
				+ PLAYER_INVENTORY_SIZE, false);
	}

	@Override
	public ItemStack slotClick(final int slotIndex, final int button, final int modifier,
			final EntityPlayer player) {
		if (player == null) {
			return null;
		}

		final Slot slot = slotIndex < 0 ? null : (Slot) this.inventorySlots
				.get(slotIndex);
		if (slot instanceof MultiSlot) {
			if (((MultiSlot) slot).isPhantom()) {
				return slotClickMultiSlot(slot, button, modifier, player);
			}
		}
		return super.slotClick(slotIndex, button, modifier, player);
	}

	private ItemStack slotClickMultiSlot(final Slot slot, final int mouseButton,
			final int modifier, final EntityPlayer player) {
		ItemStack stack = null;

		final ItemStack stackSlot = slot.getStack();
		if (stackSlot != null) {
			stack = stackSlot.copy();
		}

		if (mouseButton == 2) {
			fillPhantomSlot(slot, null, mouseButton, modifier);
		} else if (mouseButton == 0 || mouseButton == 1) {
			final InventoryPlayer playerInv = player.inventory;

			final ItemStack stackHeld = playerInv.getItemStack();

			if (stackSlot == null) {
				if (stackHeld != null && slot.isItemValid(stackHeld)) {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if (stackHeld == null) {
				adjustPhantomSlot(slot, mouseButton, modifier);
			} else if (slot.isItemValid(stackHeld)) {
				if (ItemStackHelper.areEqual(stackSlot, stackHeld)) {
					adjustPhantomSlot(slot, mouseButton, modifier);
				} else {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		} else if (mouseButton == 5) {
			final InventoryPlayer playerInv = player.inventory;
			final ItemStack stackHeld = playerInv.getItemStack();
			if (!slot.getHasStack()) {
				fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
			}
		}
		return stack;
	}

	protected void adjustPhantomSlot(final Slot slot, final int mouseButton, final int modifier) {
		if (!((MultiSlot) slot).canAdjustPhantom()) {
			return;
		}
		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if (modifier == 1) {
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2
					: stackSlot.stackSize * 2;
		} else {
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1
					: stackSlot.stackSize + 1;
		}

		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if (stackSlot.stackSize <= 0) {
			stackSlot = null;
		}

		slot.putStack(stackSlot);
	}

	protected void fillPhantomSlot(final Slot slot, final ItemStack stackHeld,
			final int mouseButton, final int modifier) {
		if (!((MultiSlot) slot).canAdjustPhantom()) {
			return;
		}

		if (stackHeld == null) {
			slot.putStack(null);
			return;
		}

		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}
		final ItemStack phantomStack = stackHeld.copy();
		phantomStack.stackSize = stackSize;

		slot.putStack(phantomStack);
	}
}

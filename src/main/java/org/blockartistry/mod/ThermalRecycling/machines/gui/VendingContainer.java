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

import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.machines.gui.TradeSlot.IResourceAvailableCheck;
import org.blockartistry.mod.ThermalRecycling.util.InventoryHelper;
import cofh.lib.gui.slot.SlotLocked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * A good chunk of this code was sourced from the following:
 * 
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-containers.html
 */
public final class VendingContainer extends MachineContainer<VendingTileEntity>
		implements IResourceAvailableCheck {

	public VendingContainer(final InventoryPlayer inv,
			final IInventory tileEntity) {
		super((VendingTileEntity) tileEntity);

		// GUI dimension is width 427, height 240
		final IInventory inventory = entity.getMachineInventory();

		// Add configuration slots
		for (int i = 0; i < 6; i++) {

			final int slotBase = VendingTileEntity.CONFIG_SLOT_START + i;
			final int x = (i < 3) ? 17 : 97;
			final int y = (i < 3) ? i * GUI_INVENTORY_CELL_SIZE + 17 : (i - 3)
					* GUI_INVENTORY_CELL_SIZE + 17;

			Slot slot = new SlotLocked(inventory, slotBase, x, y);
			addSlotToContainer(slot);

			slot = new SlotLocked(inventory, slotBase + 6, x
					+ GUI_INVENTORY_CELL_SIZE, y);
			addSlotToContainer(slot);

			final TradeSlot ms = new TradeSlot(inventory, slotBase + 12, x
					+ GUI_INVENTORY_CELL_SIZE * 2 + 9, y);
			ms.setInfinite().setPhantom().setResourceAvaialbleCheck(this);
			addSlotToContainer(ms);
		}

		addPlayerInventory(inv, 166);
	}

	@Override
	public ItemStack slotClick(final int slotIndex, final int button,
			final int modifier, final EntityPlayer player) {

		if (player == null) {
			return null;
		}

		final Slot slot = slotIndex < 0 ? null : (Slot) this.inventorySlots
				.get(slotIndex);
		if (slot instanceof TradeSlot) {
			if (((TradeSlot) slot).isPhantom()) {
				return doTrade(slot, player);
			}
		}

		return super.slotClick(slotIndex, button, modifier, player);
	}

	protected ItemStack doTrade(final Slot slot, final EntityPlayer player) {

		final ItemStack[] entityInventory = entity.getRawInventory();
		final ItemStack[] playerInventory = player.inventory.mainInventory;

		// Get the result of the potential trade. If there is nothing,
		// or the player inventory cannot hold the stack then return null.
		final ItemStack result = slot.getStack();
		if (result == null
				|| !InventoryHelper.canInventoryAccept(playerInventory, 0,
						playerInventory.length - 1, result, null))
			return null;

		final boolean normalMode = !entity.isAdminMode();

		// Can the inventory provide the result item?
		if (normalMode
				&& !InventoryHelper.doesInventoryContain(entityInventory,
						VendingTileEntity.INVENTORY_SLOT_START,
						VendingTileEntity.GENERAL_INVENTORY_SIZE - 1, result,
						null))
			return null;

		// Get the input slots. We do some math on the slot index to
		// figure out the right input slots.
		final int index = slot.slotNumber / 3
				+ VendingTileEntity.CONFIG_SLOT_START;
		final ItemStack input1 = entity.getStackInSlot(index);
		final ItemStack input2 = entity.getStackInSlot(index + 6);

		// See if the vending inventory can accept the required items
		if (normalMode
				&& !InventoryHelper.canInventoryAccept(entityInventory,
						VendingTileEntity.INVENTORY_SLOT_START,
						VendingTileEntity.GENERAL_INVENTORY_SIZE - 1, input1,
						input2))
			return null;

		// See if the player can provide the payment
		if (!InventoryHelper.doesInventoryContain(playerInventory, 0,
				playerInventory.length - 1, input1, input2))
			return null;

		// OK - things should work. Do the transaction.
		if (input1 != null) {
			if (!entity.isAdminMode()) {
				entity.addStackToOutput(input1.copy());
			}
			InventoryHelper.removeItemStackFromInventory(playerInventory,
					input1.copy(), 0, playerInventory.length - 1);
		}
		if (input2 != null) {
			if (!entity.isAdminMode()) {
				entity.addStackToOutput(input2.copy());
			}
			InventoryHelper.removeItemStackFromInventory(playerInventory,
					input2.copy(), 0, playerInventory.length - 1);
		}

		if (!entity.isAdminMode()) {
			entity.removeStackFromOutput(result.copy());
		}

		player.inventory.addItemStackToInventory(result.copy());
		
		// play a tink sound to signal the trade
		if(player.worldObj.isRemote)
			player.playSound("random.orb", 0.5F, 5F);
		player.onUpdate();

		return null;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn,
			final int slotIndex) {
		// No shift+click behavior in this GUI
		return null;
	}

	@Override
	public boolean isAvailable(final Slot slot) {
		if(entity.isAdminMode())
			return true;
		
		final ItemStack[] inv = entity.getRawInventory();
		final ItemStack stack = inv[slot.getSlotIndex()];
		if(stack != null)
			return InventoryHelper.doesInventoryContain(inv,
					VendingTileEntity.INVENTORY_SLOT_START,
					VendingTileEntity.GENERAL_INVENTORY_SIZE - 1, stack, null);
		
		return true;
	}
}

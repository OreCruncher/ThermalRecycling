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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.client.TextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cofh.lib.gui.GuiBase;

public final class VendingGui extends GuiBase {

	public VendingGui(final InventoryPlayer playerInventory, final IInventory entity) {
		super(new VendingContainer(playerInventory, entity),
				new ResourceLocation(ThermalRecycling.MOD_ID,
						"textures/vending_gui.png"));

		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;

		name = StatCollector.translateToLocal("tile.MachineVending.name");
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		
		// Loop through the slots looking for TradeSlots so we can
		// render the resource available background colors
		for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); i1++) {
			Slot slot = (Slot) inventorySlots.inventorySlots.get(i1);
			if(slot instanceof TradeSlot && slot.getHasStack()) {
				final TradeSlot ts = (TradeSlot) slot;
				final int x = guiLeft + ts.xDisplayPosition;
				final int y = guiTop + ts.yDisplayPosition;
				drawIcon(ts.isAvailable() ? TextureManager.slotResourceAvailable : TextureManager.slotResourceNotAvailable, x, y, 1);
			}
		}
	}
}

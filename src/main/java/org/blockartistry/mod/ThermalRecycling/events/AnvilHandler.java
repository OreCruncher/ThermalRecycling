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

package org.blockartistry.mod.ThermalRecycling.events;

import org.blockartistry.mod.ThermalRecycling.ItemManager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class AnvilHandler {

	private static final int[] EXPERIENCE_COST = { 3, 4, 5 };
	private static final int[] REPAIR_AMOUNT_SCRAP = { 15, 30, 60 };
	private static final int[] REPAIR_AMOUNT_SCRAPBOX = { 135, 270, 540 };

	private boolean isValidRepairItem(final ItemStack stack) {

		if (stack != null) {
			return stack.getItem() == ItemManager.recyclingScrap
					|| stack.getItem() == ItemManager.recyclingScrapBox;
		}

		return false;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void onAnvilChange(final AnvilUpdateEvent event) {
		
		final ItemStack itemToRepair = event.left;
		final ItemStack repairMaterial = event.right;

		if (itemToRepair == null || repairMaterial == null
				|| !itemToRepair.isItemDamaged()
				|| !isValidRepairItem(repairMaterial))
			return;

		int repairAmount = 0;

		if (repairMaterial.getItem() == ItemManager.recyclingScrap)
			repairAmount = REPAIR_AMOUNT_SCRAP[repairMaterial.getItemDamage()];
		else
			repairAmount = REPAIR_AMOUNT_SCRAPBOX[repairMaterial
					.getItemDamage()];

		// Figure out the quantity needed to fully repair the item
		final int itemDamage = itemToRepair.getItemDamage();
		int howManyUnits = itemDamage / repairAmount;
		if (itemDamage % repairAmount != 0)
			howManyUnits++;

		// Cap it
		howManyUnits = Math.min(howManyUnits, repairMaterial.stackSize);
		final int damageRepaired = Math.min(itemDamage, howManyUnits
				* repairAmount);

		event.cost = EXPERIENCE_COST[repairMaterial.getItemDamage()];
		event.materialCost = howManyUnits;
		event.output = itemToRepair.copy();
		event.output.setItemDamage(event.output.getItemDamage()
				- damageRepaired);
	}

	public AnvilHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}

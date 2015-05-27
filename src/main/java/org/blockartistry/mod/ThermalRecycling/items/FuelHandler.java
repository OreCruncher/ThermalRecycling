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

package org.blockartistry.mod.ThermalRecycling.items;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public final class FuelHandler implements IFuelHandler {

	protected void registerCarbonValue(final ItemStack stack) {
		
		final NBTTagCompound msg = new NBTTagCompound();
		msg.setBoolean("useBurnTime", true);
		msg.setTag("item", new NBTTagCompound());
		stack.writeToNBT(msg.getCompoundTag("item"));
		
		FMLInterModComms.sendMessage(SupportedMod.ADVANCED_GENERATORS.getModId(), "AddCarbonValue", msg);
	}
	
	public FuelHandler() {
		GameRegistry.registerFuelHandler(this);
		
		if(SupportedMod.ADVANCED_GENERATORS.isLoaded()) {
			// Configure Syngas carbon sources.  IMC support
			// was added in 0.9.13.72
			registerCarbonValue(new ItemStack(ItemManager.debris));
			registerCarbonValue(new ItemStack(BlockManager.scrapBlock));
		}
	}

	@Override
	public int getBurnTime(final ItemStack fuel) {

		final Item item = fuel.getItem();
		int burn = 0;

		if (item == ItemManager.recyclingScrap
				|| item == ItemManager.recyclingScrapBox) {

			switch (fuel.getItemDamage()) {
			case RecyclingScrap.POOR:
				burn = ModOptions.getPoorScrapFuelSetting();
				break;
			case RecyclingScrap.STANDARD:
				burn = ModOptions.getStandardScrapFuelSetting();
				break;
			case RecyclingScrap.SUPERIOR:
				burn = ModOptions.getSuperiorScrapFuelSetting();
				break;
			}

			if (item == ItemManager.recyclingScrapBox)
				burn *= ModOptions.getScrapBoxMultiplier();
			
		} else if(item == ItemManager.debris) {
			burn = ModOptions.getDebrisFuelSetting();
		} else if(item == Item.getItemFromBlock(BlockManager.scrapBlock)) {
			burn = ModOptions.getScrapBlockFuelSetting();
		}

		return burn;
	}
}

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

package org.blockartistry.mod.ThermalRecycling.support;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.ItemInfo;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModMinefactoryReloaded extends ModPlugin {

	public ModMinefactoryReloaded() {
		super(SupportedMod.MINEFACTORY_RELOADED);
	}

	@Override
	public void apply() {
		
		ItemInfo.put(ItemStackHelper.getItemStack("MineFactoryReloaded:plastic.raw"), ScrapValue.NONE, false, false);
		ItemInfo.put(ItemStackHelper.getItemStack("MineFactoryReloaded:plastic.sheet"), ScrapValue.NONE, false, false);
		ItemInfo.put(ItemStackHelper.getItemStack("MineFactoryReloaded:rubberwood.sapling"), ScrapValue.NONE, false, false);
		
		// Add the rubber saplings for recycling. We don't want the big daddy.
		pulverizer
				.appendSubtypeRange("MineFactoryReloaded:rubberwood.sapling",
						0, 2, 8).output(Blocks.dirt).save();

		// Range upgrades
		ItemStack is = ItemStackHelper.getItemStack(
				"MineFactoryReloaded:upgrade.radius", 1);
		pulverizer.append(is).output("minecraft:dye:4", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 1).output("ingotTin", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 2).output("ingotIron", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 3).output("ingotCopper", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 4).output("ingotBronze", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 5).output("ingotSilver", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 6).output("ingotGold", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 7).output(Items.quartz, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 8).output(Items.diamond, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 9).output("ingotPlatinum", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 10).output(Items.emerald, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 11).output(Blocks.cobblestone, 3)
				.secondaryOutput("nuggetGold").save();

		// Laser Focii
		pulverizer.appendSubtypeRange("MineFactoryReloaded:laserfocus", 0, 15)
				.output(Items.emerald, 4).secondaryOutput("nuggetGold", 4)
				.save();

		// Syringe
		furnace.reset()
				.append("MineFactoryReloaded:syringe.empty",
						"MineFactoryReloaded:syringe.zombie",
						"MineFactoryReloaded:syringe.cure",
						"MineFactoryReloaded:syringe.growth",
						"MineFactoryReloaded:syringe.slime",
						"MineFactoryReloaded:syringe.health")
				.output("ingotIron").save();

		// Plastic
		pulverizer.append("MineFactoryReloaded:plastic.sheet")
				.output("MineFactoryReloaded:plastic.raw").save();
		pulverizer.append("MineFactoryReloaded:xpextractor")
				.output("MineFactoryReloaded:plastic.raw", 5).save();
		pulverizer
				.append("MineFactoryReloaded:straw",
						"MineFactoryReloaded:plastic.boots")
				.output("MineFactoryReloaded:plastic.raw", 4).save();
		pulverizer.append("MineFactoryReloaded:record.blank")
				.output("MineFactoryReloaded:plastic.raw", 8).save();

		// Misc
		pulverizer.append("MineFactoryReloaded:needlegun.ammo.empty")
				.output("MineFactoryReloaded:plastic.raw")
				.secondaryOutput("nuggetIron", 2).save();
		pulverizer.append("MineFactoryReloaded:spyglass")
				.output("ingotBronze", 2)
				.secondaryOutput("MineFactoryReloaded:plastic.raw", 2).save();

	}
}

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
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModMinefactoryReloaded extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
			"MineFactoryReloaded:sugarcharcoal:*",
			"MineFactoryReloaded:fertilizer:*",
			"MineFactoryReloaded:ceramicdye:*",
			"MineFactoryReloaded:stainedglass.block:*",
			"MineFactoryReloaded:stainedglass.pane:*",
			"MineFactoryReloaded:rubberwood.log:*",
			"MineFactoryReloaded:rubberwood.leaves:*",
			"MineFactoryReloaded:rubberwood.sapling:*",
			"MineFactoryReloaded:milkbottle:*",
			"MineFactoryReloaded:meat.ingot.raw",
			"MineFactoryReloaded:meat.ingot.cooked",
			"MineFactoryReloaded:meat.nugget.raw",
			"MineFactoryReloaded:meat.nugget.cooked",

	};

	static final String[] scrapValuesNone = new String[] {
			"MineFactoryReloaded:sugarcharcoal:*",
			"MineFactoryReloaded:fertilizer:*",
			"MineFactoryReloaded:ceramicdye:*",
			"MineFactoryReloaded:stainedglass.block:*",
			"MineFactoryReloaded:stainedglass.pane:*",
			"MineFactoryReloaded:plastic.cup:*",
			"MineFactoryReloaded:plastic.raw:*",
			"MineFactoryReloaded:plastic.sheet:*",
			"MineFactoryReloaded:rubberwood.log:*",
			"MineFactoryReloaded:rubberwood.leaves:*",
			"MineFactoryReloaded:rubberwood.sapling:*",
			"MineFactoryReloaded:plastic.boots:*",
			"MineFactoryReloaded:straw:*", "MineFactoryReloaded:rubber.raw:*",
			"MineFactoryReloaded:rubber.bar:*",
			"MineFactoryReloaded:vinescaffold:*",
			"MineFactoryReloaded:milkbottle:*",
			"MineFactoryReloaded:meat.ingot.raw",
			"MineFactoryReloaded:meat.ingot.cooked",
			"MineFactoryReloaded:meat.nugget.raw",
			"MineFactoryReloaded:meat.nugget.cooked", };

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] { "MineFactoryReloaded:rubberwood.sapling:1" };

	static final String[] scrapValuesSuperior = new String[] { "MineFactoryReloaded:rubberwood.sapling:2" };

	public ModMinefactoryReloaded() {
		super(SupportedMod.MINEFACTORY_RELOADED);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		ItemScrapData data = ItemScrapData.get(ItemStackHelper
				.getItemStack("MineFactoryReloaded:milkbottle"));
		data.setScrubFromOutput(true);
		ItemScrapData.put(data);

		// Add the rubber saplings for recycling. We don't want the big daddy.
		pulverizer
				.appendSubtypeRange("MineFactoryReloaded:rubberwood.sapling",
						0, 2, 8).output(Blocks.dirt).save();
	}
}

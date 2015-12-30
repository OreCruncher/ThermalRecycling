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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

public final class ModMinefactoryReloaded extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "sugarcharcoal", "fertilizer", "ceramicdye:*",
			"stainedglass.block:*", "stainedglass.pane:*", "rubberwood.log", "rubberwood.leaves:*",
			"rubberwood.sapling:*", "milkbottle", "meat.ingot.raw", "meat.ingot.cooked", "meat.nugget.raw",
			"meat.nugget.cooked", "road:*", "brick:*", "stone:*", "tank", "machineblock:0", };

	static final String[] scrapValuesNone = new String[] { "sugarcharcoal", "fertilizer", "ceramicdye:*",
			"stainedglass.block:*", "stainedglass.pane:*", "plastic.cup:*", "plastic.raw", "plastic.sheet",
			"rubberwood.log", "rubberwood.leaves:*", "rubberwood.sapling:*", "plastic.boots", "straw", "rubber.raw",
			"rubber.bar", "vinescaffold", "milkbottle", "meat.ingot.raw", "meat.ingot.cooked", "meat.nugget.raw",
			"meat.nugget.cooked", "ruler", "record.blank", "plastic.bag", "safarinet.singleuse", "hammer", "road:*",
			"brick:*", "stone:*", "tank", "machineblock:0", "cable.plastic", "farmland", "potatolauncher",
			"xpextractor" };

	static final String[] scrapValuesPoor = new String[] { "rednet.memorycard", "detcord", "needlegun.ammo.empty",
			"safarinet.jailer", "rail.passenger.dropoff", "rail.passenger.pickup", "rednet.meter:0" };

	static final String[] scrapValuesStandard = new String[] { "rubberwood.sapling:1" };

	static final String[] scrapValuesSuperior = new String[] { "rubberwood.sapling:2", "portaspawner",
			"upgrade.logic:2", "rednet.logic", "syringe.cure", "needlegun", "rocketlauncher", "needlegun.ammo.anvil" };

	public ModMinefactoryReloaded() {
		super(SupportedMod.MINEFACTORY_RELOADED);
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrubFromOutput("milkbottle");

		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		registerPulverizeToDirt("rubberwood.sapling", 0, 3);

		// Glass
		pulverizer.setEnergy(3200).appendSubtypeRange("MineFactoryReloaded:stainedglass.pane", 0, 15, 8)
				.output(Blocks.sand, 3).save();
		pulverizer.setEnergy(3200).appendSubtypeRange("MineFactoryReloaded:stainedglass.block", 0, 15)
				.output(Blocks.sand).save();

		// Register some stuff
		registerPileOfRubbleDrop(1, 2, 4, "meat.ingot.cooked", "rubber.bar");
		registerPileOfRubbleDrop(1, 1, 2, "plastic.boots");

		return true;
	}
}

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

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.handlers.BuildCraftGateScrapHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModBuildCraftSilicon extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "redstone_board", };

	static final String[] scrapValuesNone = new String[] { "redstoneChipset", "redstone_board" };

	static final String[] scrapValuesPoor = new String[] { "redstoneChipset:5",
			"redstoneChipset:6" };

	static final String[] scrapValuesStandard = new String[] {
			"redstoneChipset:1", "redstoneChipset:2", "redstoneChipset:4" };

	static final String[] scrapValuesSuperior = new String[] {
			"redstoneChipset:3", "redstoneChipset:7", "laserBlock",
			"laserTableBlock:0", "laserTableBlock:1", "laserTableBlock:2",
			"laserTableBlock:4", "zonePlan", "robot" };

	public ModBuildCraftSilicon() {
		super(SupportedMod.BUILDCRAFT_SILICON);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		BuildCraftGateScrapHandler handler = new BuildCraftGateScrapHandler();
		ScrapHandler
				.registerHandler(ItemStackHelper
						.getItemStack("BuildCraft|Transport:pipeGate:*"),
						handler);
	}
}

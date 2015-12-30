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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

public class ModImmersiveEngineering extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {};

	static final String[] scrapValuesNone = new String[] { "woodenDecoration:*", "hemp", "material:0", "material:1",
			"material:2", "material:3", "material:4", "material:5", "material:7", "material:10", "seed:*",
			"woodenStairs", "woodenDevice:*", "tool:3", "metalDecoration:0", "metalDecoration:1", "metalDecoration:3",
			"metalDecoration:9", "metalDecoration:8", "coil:3", "coil:4", "drillhead:0" };

	static final String[] scrapValuesPoor = new String[] { "metalDevice:0", "metalDevice:2", "metalDevice:5",
			"metalDevice:6", "metalDevice:11", "material:13", "fluidContainers:0", "fluidContainers:2",
			"fluidContainers:6", "material:6", "storageSlab:*", "metalDecoration:2", "coil:2", "bullet:1",

			// Nuggets
			"metal:21", "metal:22", "metal:23", "metal:24", "metal:25", "metal:26", "metal:27", "metal:28",
			"metal:29" };

	static final String[] scrapValuesStandard = new String[] { "woodenDevice:2" };

	static final String[] scrapValuesSuperior = new String[] { "skyhook", "toolupgrade:0", "toolupgrade:1",
			"toolupgrade:3", "toolupgrade:5", "metalDevice:8", "metalDevice:12", "metalDevice:13", "metalDevice:14",
			"revolver:0", "drill:0" };

	public ModImmersiveEngineering() {
		super(SupportedMod.IMMERSIVE_ENGINEERING);
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		return true;
	}
}

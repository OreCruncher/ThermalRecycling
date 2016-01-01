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
			"material:2", "material:3", "material:4", "material:5", "seed:*", "woodenStairs", "woodenDevice:*",
			"tool:3", "stoneDevice:*", "coil:3", "material:6", };

	static final String[] scrapValuesPoor = new String[] { "metalDevice:0", "metalDevice:2", "metalDevice:5",
			"metalDevice:6", "metalDevice:11", "material:13", "fluidContainers:0", "fluidContainers:2",
			"fluidContainers:6", "metalDecoration:2", "bullet:1", "metalDecoration:0", "metalDecoration:1",
			"metalDecoration:8", "metalDecoration:9", "stoneDevice:2",
			// Nuggets
			"metal:21", "metal:22", "metal:23", "metal:24", "metal:25", "metal:26", "metal:27", "metal:28",
			"metal:29" };

	static final String[] scrapValuesStandard = new String[] { "woodenDevice:2", "woodenDevice:3", "material:7",
			"metalDecoration:3", "storageSlab:*", "material:10", "storageSlab:0", "storageSlab:3", "drillhead:0",
			"metalDevice:1", "metalDevice:3", "toolupgrade:4", "stoneDevice:4", };

	static final String[] scrapValuesSuperior = new String[] { "storage:*", "skyhook", "toolupgrade:*", "metalDevice:*",
			"drillhead:1", "revolver:0", "drill:0", "metalDecoration:5", "metalDecoration:6", "metalDecoration:7",
			"metalMultiblock:2", "material:8", "metalMultiblock:3", "metalMultiblock:0", };

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

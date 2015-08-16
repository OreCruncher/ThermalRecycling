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

	static final String[] recipeIgnoreList = new String[] { };

	static final String[] scrapValuesNone = new String[] {
			"woodenDecoration:*", "hemp", "material:0", "material:1",
			"material:2", "material:3", "material:4", "material:5",
			"seed:*"
	};

	static final String[] scrapValuesPoor = new String[] {
			"metalDevice:0", "metalDevice:2", "metalDevice:5", "metalDevice:6",
			"metalDevice:11", "woodenDevice:0", "woodenDevice:5", "material:13",
			"tool:3", "fluidContainers:0", "fluidContainers:2", "fluidContainers:6",
			"material:6", "metalDecoration:0", "metalDecoration:8",
			"metalDecoration:9",
			
			// Nuggets
			"metal:21", "metal:22", "metal:23", "metal:24", "metal:25", "metal:26",
			"metal:27", "metal:28", "metal:29"
	};

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] {
			};

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

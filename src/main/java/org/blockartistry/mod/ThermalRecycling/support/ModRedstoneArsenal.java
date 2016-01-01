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

public class ModRedstoneArsenal extends ModPlugin {

	private static final String[] recipeIgnoreList = new String[] { "Storage:*", "material:64", "material:96", };

	private static final String[] scrapValuesNone = new String[] {

	};

	private static final String[] scrapValuesPoor = new String[] { "material:64" };

	private static final String[] scrapValuesStandard = new String[] {};

	// Have to expand storage 0 and 1 to override the
	// ore dictionary block scan during boot.
	private static final String[] scrapValuesSuperior = new String[] { "Storage:0", "Storage:1", "armor.plateFlux",
			"armor.helmetFlux", "armor.legsFlux", "armor.bootsFlux", "material:96", "material:193", "material:128",
			"tool.wrenchFlux", "tool.pickaxeFlux", "tool.swordFlux", "tool.shovelFlux", "tool.axeFlux",
			"tool.sickleFlux", "tool.battleWrenchFlux", };

	public ModRedstoneArsenal() {
		super(SupportedMod.REDSTONE_ARSENAL);
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

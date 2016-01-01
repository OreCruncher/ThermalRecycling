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

public final class ModSimplyJetpacks extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "particleCustomizers:*", "components:66" };

	static final String[] scrapValuesNone = new String[] { "particleCustomizers:*", "jetpacksCommon:9001" };

	static final String[] scrapValuesPoor = new String[] { "components:66", "jetpacksCommon:0" };

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] { "jetpacks:1", "jetpacks:2", "jetpacks:3", "jetpacks:4",
			"jetpacks:101", "jetpacks:102", "jetpacks:103", "jetpacks:104", "jetpacks:5", "components:11",
			"components:12", "components:13", "components:14", "components:15", "components:60", "components:61",
			"components:62", "components:63", "components:67", "components:68", "components:69", "fluxpacks:1",
			"fluxpacks:2", "fluxpacks:3", "fluxpacks:103", "fluxpacks:4", "fluxpacks:104", "fluxpacks:102",
			"armorPlatings:1", "armorPlatings:4" };

	public ModSimplyJetpacks() {
		super(SupportedMod.SIMPLY_JETPACKS);
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

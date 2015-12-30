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

public final class ModRFDrills extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {};

	static final String[] scrapValuesNone = new String[] {

	};

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] { "motor_te:0", "motor_te:1" };

	static final String[] scrapValuesSuperior = new String[] { "motor_te:2", "motor_te:4", "motor_te:5", "motor_te:6",
			"redstone_chainsaw", "redstone_drill", "resonant_drill", "flux_infused_crusher", "leadstone_chainsaw",
			"hardened_chainsaw", "leadstone_drill", "hardened_drill", "resonant_chainsaw", "motor_te:3", };

	public ModRFDrills() {
		super(SupportedMod.RFDRILLS);
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Drain the motors
		fluid.setEnergy(16000).append("rfdrills:motor_te:2").output("rfdrills:motor_te:3").fluid("redstone", 4000)
				.save();

		fluid.setEnergy(16000).append("rfdrills:motor_te:4").output("rfdrills:motor_te:5").fluid("cryotheum", 4000)
				.save();

		return true;
	}
}

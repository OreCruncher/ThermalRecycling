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

import cpw.mods.fml.common.Loader;

public final class ModBuildCraftCore extends ModPlugin {

	public ModBuildCraftCore() {
		super(SupportedMod.BUILDCRAFT_CORE);
	}

	@Override
	public boolean initialize() {
		
		final ItemDefinitions definitions = ItemDefinitions.load(getModId());
		makeRegistrations(definitions);

		// Gears - metalic gears handled via Thermal Expansion
		sawmill.append("BuildCraft|Core:woodenGearItem").output("dustWood", 4).save();

		if (Loader.isModLoaded("ExtraTiC")) {
			// Gold gears can be made with 4 gold ingots using ExtraTiC
			// So we remove the iron gear part of the decomp recipe to
			// avoid infinite material if the player can somehow
			// manage to get BC related Gold Gears.
			recycler.useRecipe("BuildCraft|Core:goldGearItem").scrubOutput("gearIron").save();
		}

		return true;
	}
}

/*
 * This file is part of ModpackInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 * Copyright (c) contributors
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

package org.blockartistry.mod.ThermalRecycling;

import net.minecraftforge.common.config.Configuration;

public final class ModOptions {

	protected static final String CATEGORY_RECYCLE_RECIPIES_CONTROL = "recycle.recipe.control";
	protected static final String CONFIG_ENABLE_DIAMOND_RECIPIES = "Enable Diamond Recycling";
	protected static final String CONFIG_ENABLE_NETHER_STAR_RECIPIES = "Enable Nether Star Recycling";

	protected boolean enableDiamondRecycle = true;
	protected boolean enableNetherStarRecycle = true;

	public void load(Configuration config) {

		enableDiamondRecycle = config.getBoolean("Enable Diamond Recycling",
				CATEGORY_RECYCLE_RECIPIES_CONTROL, enableDiamondRecycle,
				"Controls whether recycling items for diamonds is enabled");

		enableNetherStarRecycle = config.getBoolean(
				"Enable Nether Star Recycling",
				CATEGORY_RECYCLE_RECIPIES_CONTROL, enableNetherStarRecycle,
				"Controls whether recycling items for nether stars is enabled");

	}

	public boolean getEnableDiamondRecycle() {
		return enableDiamondRecycle;
	}

	public boolean getEnableNetherStarRecycle() {
		return enableNetherStarRecycle;
	}
}

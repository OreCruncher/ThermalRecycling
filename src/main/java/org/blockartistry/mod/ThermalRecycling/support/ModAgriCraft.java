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

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import com.InfinityRaider.AgriCraft.api.API;
import com.InfinityRaider.AgriCraft.api.APIBase;
import com.InfinityRaider.AgriCraft.api.v1.APIv1;
import com.InfinityRaider.AgriCraft.api.v1.BlockWithMeta;

import net.minecraft.item.ItemStack;

public class ModAgriCraft extends ModPlugin {

	private static final int AGRICRAFT_API_VERSION = 1;

	static final String[] recipeIgnoreList = new String[] { "journal", "waterChannelFull:0" };

	static final String[] scrapValuesNone = new String[] { "waterTank:0", "waterChannel:0", "waterChannelFull:0",
			"cropsItem", "magnifyingGlass", "handRake:0", "seedAnalyzer", "grate:0", "fenceGate:0", "fence:0",
			"journal" };

	static final String[] scrapValuesPoor = new String[] { "nuggetQuartz", "handRake:1" };

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] {};

	public ModAgriCraft() {
		super(SupportedMod.AGRICRAFT);
	}

	private static APIBase getApi() {

		try {

			APIBase api = API.getAPI(AGRICRAFT_API_VERSION);
			if (api.getVersion() >= AGRICRAFT_API_VERSION)
				return api;

		} catch (Throwable t) {

		}

		return null;
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Process the seed list
		for (ItemStack item : OreDictionaryHelper.getOres("listAllseed")) {
			ItemData.setValue(item, ScrapValue.NONE);
			ItemData.setCompostIngredientValue(item, CompostIngredient.BROWN);
		}

		// Register our soil block for use by agricraft
		boolean success = false;
		try {

			final APIBase api = getApi();
			if (api instanceof APIv1) {
				final APIv1 v1 = (APIv1) api;
				success = v1.registerDefaultSoil(new BlockWithMeta(BlockManager.fertileLand, 0));
			}

		} catch (Throwable t) {
		}

		if (!success)
			ModLog.warn("Unable to register soil block with AgriCraft API");

		return true;
	}
}

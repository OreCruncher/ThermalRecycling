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

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

public class ModExtrabiomesXL extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
			"terrain_blocks1:*", "log1:*", "log2:*", "mini_log_1",
			"cornerlog_baldcypress", "cornerlog_rainboweucalyptus",
			"cornerlog_oak", "cornerlog_fir", "cornerlog_redwood",
			"log_elbow_baldcypress", "log_elbow_rainbow_eucalyptus", "vines",
			"waterplant1:*", "quicksand", "planks:*", "woodslab2:*",
			"double_woodslab2:*", "woodslab:*", "double_woodslab:*",
			"slabRedRock:*", "double_slabRedRock:*", "stairsRedCobble",
			"stairs.redwood", "stairs.fir", "stairs.acacia",
			"stairs.rainboweucalyptus", "stairs.cypress", "stairs.baldcypress",
			"stairs.japanesemaple", "stairs.autumn", "stairs.sakurablossom",
			"redrockbrick", "wall:*", "fence:*", "door_acacia", "door_autumn",
			"door_baldcypress", "door_cypress", "door_fir",
			"door_japanesemaple", "door_rainboweucalyptus", "door_redwood",
			"door_sakura", "fencegate_acacia", "fencegate_autumn",
			"fencegate_cypress", "fencegate_baldcypress", "fencegate_fir",
			"fencegate_japanesemaple", "fencegate_rainboweucalyptus",
			"fencegate_redwood", "fencegate_sakura", "extrabiomes.logturner",
			"extrabiomes.dye:*", "extrabiomes.food:*", "extrabiomes.crop:*",
			"extrabiomes.seed:*", "extrabiomes.scarecrow", "extrabiomes.paste", };

	static final String[] scrapValuesNone = new String[] { "leaves_1:*",
			"leaves_2:*", "leaves_3:*", "leaves_4:*", "plants4",
			"terrain_blocks1:*", "terrain_blocks2", "flower1:*", "flower2:*",
			"flower3:*", "grass:*", "leaf_pile", "saplings_1:*",
			"saplings_2:*", "mini_log_1", "cornerlog_baldcypress",
			"cornerlog_rainboweucalyptus", "cornerlog_oak", "cornerlog_fir",
			"cornerlog_redwood", "log_elbow_baldcypress",
			"log_elbow_rainbow_eucalyptus", "waterplant1:*", "quicksand",
			"planks:*", "woodslab2:*", "double_woodslab2:*", "woodslab:*",
			"double_woodslab:*", "slabRedRock:*", "double_slabRedRock:*",
			"stairsRedCobble", "stairs.redwood", "stairs.fir", "stairs.acacia",
			"stairs.rainboweucalyptus", "stairs.cypress", "stairs.baldcypress",
			"stairs.japanesemaple", "stairs.autumn", "stairs.sakurablossom",
			"redrockbrick", "wall:*", "fence:*", "door_acacia", "door_autumn",
			"door_baldcypress", "door_cypress", "door_fir",
			"door_japanesemaple", "door_rainboweucalyptus", "door_redwood",
			"door_sakura", "fencegate_acacia", "fencegate_autumn",
			"fencegate_cypress", "fencegate_baldcypress", "fencegate_fir",
			"fencegate_japanesemaple", "fencegate_rainboweucalyptus",
			"fencegate_redwood", "fencegate_sakura", "extrabiomes.logturner",
			"extrabiomes.dye:*", "extrabiomes.food:*", "extrabiomes.crop:*",
			"extrabiomes.seed:*", "extrabiomes.scarecrow", "extrabiomes.paste", };

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] {

	};

	public ModExtrabiomesXL() {
		super(SupportedMod.EXTRABIOMESXL);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		registerRecycleToWoodDust(1, "log1:*", "log2:*", "mini_log_1",
				"cornerlog_baldcypress", "cornerlog_rainboweucalyptus",
				"cornerlog_oak", "cornerlog_fir", "cornerlog_redwood",
				"log_elbow_baldcypress", "log_elbow_rainbow_eucalyptus");

		registerRecycleToWoodDust(2, "planks:*", "double_woodslab2:*");

		registerRecycleToWoodDust(8, "saplings_1:*", "saplings_2:*");

		registerCompostIngredient(CompostIngredient.BROWN, "leaves_1:*",
				"leaves_2:*", "leaves_3:*", "leaves_4:*", "leaf_pile",
				"saplings_1:*", "saplings_2:*", "vines");

		registerCompostIngredient(CompostIngredient.GREEN, "plants4",
				"flower1:*", "flower2:*", "flower3:*", "grass:*");
	}
}

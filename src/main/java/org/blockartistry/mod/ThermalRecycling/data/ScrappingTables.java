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

package org.blockartistry.mod.ThermalRecycling.data;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public final class ScrappingTables {

	static final ItemStack keep = new ItemStack(Blocks.dirt);
	static final ItemStack dust = new ItemStack(Blocks.cobblestone);

	public static boolean keepIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(ScrappingTables.keep);
	}

	public static boolean dustIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(ScrappingTables.dust);
	}

	static final ItemStack debris = new ItemStack(ItemManager.debris);
	static final ItemStack poorScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.POOR);
	static final ItemStack standardScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD);
	static final ItemStack superiorScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR);

	static final ArrayList<ItemStackWeightTable> componentScrap = new ArrayList<ItemStackWeightTable>();
	static final ArrayList<ItemStackWeightTable> extractionDust = new ArrayList<ItemStackWeightTable>();

	static {

		// Each scrap quality has two tables in componentScrap. The first is for
		// when
		// an item is a component of a device that is being taken apart. The
		// second
		// table is when the item is being directly scrapped.

		// The "NONE" scrap value. This is used when breaking a recipe down and
		// this item is one of the components.
		ItemStackWeightTable t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 15));
		t.add(t.new ItemStackItem(debris, 14));
		t.add(t.new ItemStackItem(keep, 1));
		t.add(t.new ItemStackItem(dust, 2));
		t.add(t.new ItemStackItem(poorScrap, 1));
		componentScrap.add(t);

		// The "NONE" must scrap table. No scrap to be had.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 80));
		t.add(t.new ItemStackItem(debris, 20));
		componentScrap.add(t);

		// The "POOR" scrap table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 47));
		t.add(t.new ItemStackItem(debris, 46));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 12));
		t.add(t.new ItemStackItem(standardScrap, 4));
		componentScrap.add(t);

		// The "POOR" scrap MUST table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 78));
		t.add(t.new ItemStackItem(debris, 77));
		t.add(t.new ItemStackItem(poorScrap, 32));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 20));
		componentScrap.add(t);

		// The "STANDARD" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 34));
		t.add(t.new ItemStackItem(debris, 34));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 8));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		componentScrap.add(t);

		// The "STANDARD" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 65));
		t.add(t.new ItemStackItem(debris, 65));
		t.add(t.new ItemStackItem(poorScrap, 28));
		t.add(t.new ItemStackItem(standardScrap, 48));
		t.add(t.new ItemStackItem(superiorScrap, 22));
		componentScrap.add(t);

		// The "SUPERIOR" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 17));
		t.add(t.new ItemStackItem(debris, 16));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 4));
		t.add(t.new ItemStackItem(standardScrap, 16));
		t.add(t.new ItemStackItem(superiorScrap, 48));
		componentScrap.add(t);

		// The "SUPERIOR" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 48));
		t.add(t.new ItemStackItem(debris, 47));
		t.add(t.new ItemStackItem(poorScrap, 24));
		t.add(t.new ItemStackItem(standardScrap, 36));
		t.add(t.new ItemStackItem(superiorScrap, 68));
		componentScrap.add(t);

		// Extraction tables - first table is a dummy because
		// the logic below assumes there is an entry for NONE
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 1));
		extractionDust.add(t);

		// POOR
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 113));
		t.add(t.new ItemStackItem(standardScrap, 3));
		t.add(t.new ItemStackItem(ItemStackHelper.dustWood, 3));
		t.add(t.new ItemStackItem(ItemStackHelper.boneMeal, 3));
		t.add(t.new ItemStackItem(ItemStackHelper.dustIron, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustTin, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCopper, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSilver, 1));
		t.add(t.new ItemStackItem(ItemStackHelper.dustLead, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustNickel, 2));
		extractionDust.add(t);

		// STANDARD
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 30));
		t.add(t.new ItemStackItem(superiorScrap, 3));
		t.add(t.new ItemStackItem(ItemStackHelper.boneMeal, 1));
		t.add(t.new ItemStackItem(ItemStackHelper.dustIron, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustTin, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCopper, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSilver, 1));
		t.add(t.new ItemStackItem(ItemStackHelper.dustLead, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustNickel, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustGold, 1));
		extractionDust.add(t);

		// SUPERIOR
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 9));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSilver, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustGold, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustPlatinum, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustElectrum, 2));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSignalum, 1));
		t.add(t.new ItemStackItem(ItemStackHelper.dustLumium, 1));
		t.add(t.new ItemStackItem(ItemStackHelper.dustEnderium, 1));
		extractionDust.add(t);
	}

	public static ItemStackWeightTable getTable(ItemStack core, ItemStack stack) {

		boolean mustScrap = core == null;
		int scrappingValue = ItemScrapData.get(stack).getScrapValue().ordinal();

		if (!ProcessingCorePolicy.isExtractionCore(core))
			scrappingValue *= 2;

		if (mustScrap)
			scrappingValue++;

		if (core == null || ProcessingCorePolicy.isDecompositionCore(core))
			return componentScrap.get(scrappingValue);

		return extractionDust.get(scrappingValue);
	}

	public static boolean canBeScrapped(ItemStack stack) {

		return !(ItemHelper.isBlock(stack) || ItemHelper.isDust(stack)
				|| ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack)
				|| ItemHelper.isOre(stack)
				|| stack.getItem() == ItemManager.recyclingScrap
				|| stack.getItem() == ItemManager.recyclingScrapBox || stack
					.getItem() == ItemManager.debris);
	}

	public static List<ItemStack> scrapItems(ItemStack core, ItemStack stack) {
		return ScrapHandler.getHandler(stack).scrapItems(core, stack);
	}

	public static ScrapHandler.PreviewResult preview(ItemStack core,
			ItemStack stack) {
		return ScrapHandler.getHandler(stack).preview(core, stack);
	}

	public static void writeDiagnostic(Writer writer) throws Exception {

		writer.write("\n================\nScrap Tables\n================\n");

		componentScrap.get(0).diagnostic("Core: Decomposition|Scrap None",
				writer);
		componentScrap.get(1).diagnostic("No Core|Scrap None", writer);
		componentScrap.get(2).diagnostic("Core: Decomposition|Poor Scrap",
				writer);
		componentScrap.get(3).diagnostic("No Core|Poor Scrap", writer);
		componentScrap.get(4).diagnostic("Core: Decomposition|Standard Scrap",
				writer);
		componentScrap.get(5).diagnostic("No Core|Standard Scrap", writer);
		componentScrap.get(6).diagnostic("Core: Decomposition|Superior Scrap",
				writer);
		componentScrap.get(7).diagnostic("No Core|Superior Scrap", writer);

		extractionDust.get(1).diagnostic("Core: Extraction|Poor Scrap", writer);
		extractionDust.get(2).diagnostic("Core: Extraction|Standard Scrap",
				writer);
		extractionDust.get(3).diagnostic("Core: Extraction|Superior Scrap",
				writer);
	}
}
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
import org.blockartistry.mod.ThermalRecycling.items.ProcessingCore;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.TwoDimensionalArrayList;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public final class ScrappingTables {

	static final ItemStack keep = new ItemStack(Blocks.dirt);
	static final ItemStack dust = new ItemStack(Blocks.cobblestone);

	public static boolean destroyIt(ItemStack stack) {
		return stack == null;
	}
	
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


	static final List<ItemStackWeightTable> mustScrap = new ArrayList<ItemStackWeightTable>();
	static final TwoDimensionalArrayList<ItemStackWeightTable> dcompScrap = new TwoDimensionalArrayList<ItemStackWeightTable>();
	static final TwoDimensionalArrayList<ItemStackWeightTable> extractDust = new TwoDimensionalArrayList<ItemStackWeightTable>();
	
	static {
		
		ItemStackWeightTable ethereal = new ItemStackWeightTable();
		ethereal.add(ethereal.new ItemStackItem(keep,1));
		dcompScrap.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_ETHEREAL, ethereal);
		dcompScrap.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_ETHEREAL, ethereal);
		dcompScrap.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_ETHEREAL, ethereal);
		dcompScrap.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_ETHEREAL, ethereal);

		ItemStackWeightTable t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 21));
		t.add(t.new ItemStackItem(debris, 7));
		t.add(t.new ItemStackItem(keep, 1));
		t.add(t.new ItemStackItem(dust, 2));
		t.add(t.new ItemStackItem(poorScrap, 1));
		dcompScrap.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 11));
		t.add(t.new ItemStackItem(debris, 4));
		t.add(t.new ItemStackItem(keep, 1));
		t.add(t.new ItemStackItem(dust, 2));
		t.add(t.new ItemStackItem(poorScrap, 1));
		dcompScrap.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_HARDENED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 6));
		t.add(t.new ItemStackItem(debris, 2));
		t.add(t.new ItemStackItem(keep, 2));
		t.add(t.new ItemStackItem(dust, 1));
		t.add(t.new ItemStackItem(poorScrap, 1));
		dcompScrap.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_REINFORCED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(keep, 25));
		t.add(t.new ItemStackItem(dust, 5));
		t.add(t.new ItemStackItem(poorScrap, 10));
		dcompScrap.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_RESONANT, t);

		// The "NONE" must scrap table. No scrap to be had.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 80));
		t.add(t.new ItemStackItem(debris, 20));
		mustScrap.add(t);

		// The "POOR" scrap table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 47));
		t.add(t.new ItemStackItem(debris, 46));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 12));
		t.add(t.new ItemStackItem(standardScrap, 4));
		dcompScrap.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 24));
		t.add(t.new ItemStackItem(debris, 23));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 12));
		t.add(t.new ItemStackItem(standardScrap, 4));
		dcompScrap.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_HARDENED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 12));
		t.add(t.new ItemStackItem(debris, 12));
		t.add(t.new ItemStackItem(keep, 93));
		t.add(t.new ItemStackItem(dust, 31));
		t.add(t.new ItemStackItem(poorScrap, 12));
		t.add(t.new ItemStackItem(standardScrap, 4));
		dcompScrap.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_REINFORCED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(keep, 108));
		t.add(t.new ItemStackItem(dust, 16));
		t.add(t.new ItemStackItem(poorScrap, 12));
		t.add(t.new ItemStackItem(standardScrap, 4));
		dcompScrap.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_RESONANT, t);

		// The "POOR" scrap MUST table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 78));
		t.add(t.new ItemStackItem(debris, 77));
		t.add(t.new ItemStackItem(poorScrap, 32));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 20));
		mustScrap.add(t);

		// The "STANDARD" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 34));
		t.add(t.new ItemStackItem(debris, 34));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 8));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		dcompScrap.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 17));
		t.add(t.new ItemStackItem(debris, 17));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 8));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		dcompScrap.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_HARDENED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 9));
		t.add(t.new ItemStackItem(debris, 9));
		t.add(t.new ItemStackItem(keep, 93));
		t.add(t.new ItemStackItem(dust, 31));
		t.add(t.new ItemStackItem(poorScrap, 8));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		dcompScrap.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_REINFORCED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(keep, 108));
		t.add(t.new ItemStackItem(dust, 16));
		t.add(t.new ItemStackItem(poorScrap, 8));
		t.add(t.new ItemStackItem(standardScrap, 24));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		dcompScrap.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_RESONANT, t);

		// The "STANDARD" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 65));
		t.add(t.new ItemStackItem(debris, 65));
		t.add(t.new ItemStackItem(poorScrap, 28));
		t.add(t.new ItemStackItem(standardScrap, 48));
		t.add(t.new ItemStackItem(superiorScrap, 22));
		mustScrap.add(t);

		// The "SUPERIOR" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 17));
		t.add(t.new ItemStackItem(debris, 16));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 4));
		t.add(t.new ItemStackItem(standardScrap, 16));
		t.add(t.new ItemStackItem(superiorScrap, 48));
		dcompScrap.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 9));
		t.add(t.new ItemStackItem(debris, 8));
		t.add(t.new ItemStackItem(keep, 62));
		t.add(t.new ItemStackItem(dust, 62));
		t.add(t.new ItemStackItem(poorScrap, 4));
		t.add(t.new ItemStackItem(standardScrap, 16));
		t.add(t.new ItemStackItem(superiorScrap, 48));
		dcompScrap.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_HARDENED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 5));
		t.add(t.new ItemStackItem(debris, 4));
		t.add(t.new ItemStackItem(keep, 93));
		t.add(t.new ItemStackItem(dust, 31));
		t.add(t.new ItemStackItem(poorScrap, 4));
		t.add(t.new ItemStackItem(standardScrap, 16));
		t.add(t.new ItemStackItem(superiorScrap, 48));
		dcompScrap.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_REINFORCED, t);

		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(keep, 108));
		t.add(t.new ItemStackItem(dust, 16));
		t.add(t.new ItemStackItem(poorScrap, 4));
		t.add(t.new ItemStackItem(standardScrap, 16));
		t.add(t.new ItemStackItem(superiorScrap, 48));
		dcompScrap.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_RESONANT, t);

		// The "SUPERIOR" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 48));
		t.add(t.new ItemStackItem(debris, 47));
		t.add(t.new ItemStackItem(poorScrap, 24));
		t.add(t.new ItemStackItem(standardScrap, 36));
		t.add(t.new ItemStackItem(superiorScrap, 68));
		mustScrap.add(t);

		// Extraction tables - first table is a dummy because
		// the logic below assumes there is an entry for NONE
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 1));
		extractDust.setElement(ScrapValue.NONE.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		// POOR
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 120));
		t.add(t.new ItemStackItem(standardScrap, 60));
		t.add(t.new ItemStackItem(ItemStackHelper.boneMeal, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCoal, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCharcoal, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.sulfer, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustIron, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustTin, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCopper, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustNickel, 20));
		extractDust.setElement(ScrapValue.POOR.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		// STANDARD
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 78));
		t.add(t.new ItemStackItem(superiorScrap, 52));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCoal, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.niter, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustIron, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustTin, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustCopper, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSilver, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustLead, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustGold, 10));
		extractDust.setElement(ScrapValue.STANDARD.ordinal(), ProcessingCore.LEVEL_BASIC, t);

		// SUPERIOR
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 73));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSilver, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustGold, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustPlatinum, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustElectrum, 20));
		t.add(t.new ItemStackItem(ItemStackHelper.dustSignalum, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustLumium, 10));
		t.add(t.new ItemStackItem(ItemStackHelper.dustEnderium, 10));
		extractDust.setElement(ScrapValue.SUPERIOR.ordinal(), ProcessingCore.LEVEL_BASIC, t);
	}

	public static ItemStackWeightTable getTable(ItemStack core, ItemStack stack) {

		int scrappingValue = ItemScrapData.get(stack).getScrapValue().ordinal();

		// Safety check - if there is a core item past in, but its not a
		// core, assume no core.
		if(core != null && core.getItem() != ItemManager.processingCore)
			core = null;
		
		if(core == null)
			return mustScrap.get(scrappingValue);

		int coreLevel = ItemStackHelper.getItemLevel(core);
		TwoDimensionalArrayList<ItemStackWeightTable> tables = dcompScrap;
		
		if (ProcessingCorePolicy.isExtractionCore(core))
			tables = extractDust;

		return tables.get(scrappingValue, coreLevel);
	}

	public static boolean canBeScrapped(ItemStack stack) {

		return !(ItemHelper.isBlock(stack) || ItemHelper.isDust(stack)
				|| ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack)
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
	
	private static final String[] UPGRADE_NAMES = new String[] {
		"Basic", "Hardened", "Reinforced", "Resonant", "Ethereal",
	};
	
	private static void dumpTable(Writer writer, String title, List<ItemStackWeightTable> mustscrap2) throws Exception {
		writer.write(String.format("Scrap Value [%s]:\n", title));
		for(int i = 0; i < mustscrap2.size(); i++) {
			ItemStackWeightTable t = mustscrap2.get(i);
			if(t != null)
				t.diagnostic(UPGRADE_NAMES[i], writer);
		}
	}
	
	private static void dumpTable(Writer writer, String title, TwoDimensionalArrayList<ItemStackWeightTable> table) throws Exception {
		
		writer.write(String.format("Table [%s]\n", title));
		for(ScrapValue sv: ScrapValue.values()) {
			ArrayList<ItemStackWeightTable> a = table.getElementSegment(sv.ordinal());
			if(a != null) {
				dumpTable(writer, sv.name(), a);
			}
		}
	}
	
	public static void writeDiagnostic(Writer writer) throws Exception {

		writer.write("\n================\nScrap Tables\n================\n");
		dumpTable(writer, "Core: Decomposition", dcompScrap);
		dumpTable(writer, "Core: Extraction", extractDust);
		dumpTable(writer, "Must Scrap", mustScrap);
	}
}
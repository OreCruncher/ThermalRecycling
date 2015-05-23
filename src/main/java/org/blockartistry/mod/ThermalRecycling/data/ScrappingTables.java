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

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.util.JarConfiguration;
import org.blockartistry.mod.ThermalRecycling.util.Matrix2D;

import com.google.common.base.Optional;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

public final class ScrappingTables {

	private static final String[] UPGRADE_NAMES = new String[] { "Basic",
			"Hardened", "Reinforced", "Resonant", "Ethereal", };

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
	
	static final Matrix2D<ItemStackWeightTable> dcompScrap = new Matrix2D<ItemStackWeightTable>(ScrapValue.values().length, UPGRADE_NAMES.length);
	static final Matrix2D<ItemStackWeightTable> extractDust = new Matrix2D<ItemStackWeightTable>(ScrapValue.values().length, UPGRADE_NAMES.length);
	
	static ItemStackItem getItemStackItem(ItemStackWeightTable table, Entry<String, Property> e) {
		ItemStackItem item = null;
		int weight = e.getValue().getInt();
		String key = e.getKey();
		
		if("destroy".equalsIgnoreCase(key))
			item = table.new ItemStackItem(null, weight);
		else if("debris".equalsIgnoreCase(key))
			item = table.new ItemStackItem(debris, weight);
		else if("keep".equalsIgnoreCase(key))
			item = table.new ItemStackItem(keep, weight);
		else if("dust".equalsIgnoreCase(key))
			item = table.new ItemStackItem(dust, weight);
		else if("poorScrap".equalsIgnoreCase(key))
			item = table.new ItemStackItem(poorScrap, weight);
		else if("standardScrap".equalsIgnoreCase(key))
			item = table.new ItemStackItem(standardScrap, weight);
		else if("superiorScrap".equalsIgnoreCase(key))
			item = table.new ItemStackItem(superiorScrap, weight);
		else {
			ItemStack stack = ItemStackHelper.getItemStack(key);
			if(stack != null)
				item = table.new ItemStackItem(stack, weight);
		}

		return item;
	}
	
	static void processTables(String prefix, Matrix2D<ItemStackWeightTable> weightTables, JarConfiguration config) {
		for (ScrapValue sv : ScrapValue.values())
			for (int i = 0; i < UPGRADE_NAMES.length; i++) {
				
				String category = prefix + "_" + sv.name() + "_" + UPGRADE_NAMES[i];
				
				ConfigCategory cc = config.getCategory(category);
				if(cc != null && !cc.isEmpty()) {
					
					ItemStackWeightTable table = new ItemStackWeightTable();
					for(Entry<String, Property> e: cc.getValues().entrySet()) {
						ItemStackItem item = getItemStackItem(table, e);
						if(item != null)
							table.add(item);
					}
					weightTables.set(sv.ordinal(), i, table);
				}
			}
	}
	
	static {

		InputStream in = ScrappingTables.class
				.getResourceAsStream("/assets/recycling/data/scrapdata.cfg");
		try {
			
			JarConfiguration config = new JarConfiguration(in);
			processTables("dcomp", dcompScrap, config);
			processTables("extract", extractDust, config);

			for(ScrapValue sv: ScrapValue.values()) {
				String category = sv.name() + "_MustScrap";
				ItemStackWeightTable table = new ItemStackWeightTable();
				
				ConfigCategory cc = config.getCategory(category);
				if(cc != null) {
					
					for(Entry<String, Property> e: cc.getValues().entrySet()) {
						ItemStackItem item = getItemStackItem(table, e);
						if(item != null)
							table.add(item);
					}
				}
				
				mustScrap.add(table);
			}
			
		} catch (Throwable t) {
			;
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (Throwable t) {
					;
				}
		}
	}

	public static Optional<ItemStackWeightTable> getTable(ItemStack core, ItemStack stack) {

		int scrappingValue = ItemScrapData.get(stack).getScrapValue().ordinal();

		// Safety check - if there is a core item past in, but its not a
		// core, assume no core.
		if (core != null && core.getItem() != ItemManager.processingCore)
			core = null;

		if (core == null)
			return Optional.fromNullable(mustScrap.get(scrappingValue));

		int coreLevel = ItemStackHelper.getItemLevel(core);
		Matrix2D<ItemStackWeightTable> tables = dcompScrap;

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

	private static void dumpTable(Writer writer, String title,
			List<ItemStackWeightTable> mustscrap2) throws Exception {

		for (int i = 0; i < mustscrap2.size(); i++) {
			ItemStackWeightTable t = mustscrap2.get(i);
			if (t != null) {
				String s = String.format("%s %s", title, UPGRADE_NAMES[i]);
				t.diagnostic(s, writer);
			}
		}
	}

	private static void dumpTable(Writer writer, String title,
			Matrix2D<ItemStackWeightTable> table)
			throws Exception {
		
		for(int i = 0; i < table.getRowCount(); i++)
			for(int j = 0; j < table.getColCount(); j++) {
				if(table.isPresent(i, j)) {
					Optional<ItemStackWeightTable> a = table.get(i, j);
					String t = String.format("%s %s_%s", title, ScrapValue.values()[i].name(), UPGRADE_NAMES[j]);
					a.get().diagnostic(t, writer);
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
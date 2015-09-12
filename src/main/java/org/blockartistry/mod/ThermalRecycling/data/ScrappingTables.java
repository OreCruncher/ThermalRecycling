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
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.util.JarConfiguration;
import org.blockartistry.mod.ThermalRecycling.util.Matrix2D;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;

import com.google.common.base.Optional;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

public final class ScrappingTables {
	
	private ScrappingTables() {}

	private static final String[] UPGRADE_NAMES = new String[] { "Basic",
			"Hardened", "Reinforced", "Resonant", "Ethereal", };

	private static final ItemStack keep = new ItemStack(Blocks.bedrock);
	private static final ItemStack dust = new ItemStack(Blocks.air);

	public static boolean destroyIt(final ItemStack stack) {
		return stack == null;
	}

	public static boolean keepIt(final ItemStack stack) {
		return stack != null && stack.isItemEqual(ScrappingTables.keep);
	}

	public static boolean dustIt(final ItemStack stack) {
		return stack != null && stack.isItemEqual(ScrappingTables.dust);
	}

	public static final ItemStack debris = new ItemStack(ItemManager.debris);
	public static final ItemStack poorScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.POOR);
	public static final ItemStack standardScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD);
	public static final ItemStack superiorScrap = new ItemStack(
			ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR);
	
	public static final ItemStack poorScrapBox = new ItemStack(ItemManager.recyclingScrapBox, 1, RecyclingScrapBox.POOR);
	public static final ItemStack standardScrapBox = new ItemStack(ItemManager.recyclingScrapBox, 1, RecyclingScrapBox.STANDARD);
	public static final ItemStack superiorScrapBox = new ItemStack(ItemManager.recyclingScrapBox, 1, RecyclingScrapBox.SUPERIOR);

	private static final List<ItemStackWeightTable> mustScrap = new ArrayList<ItemStackWeightTable>();
	private static final Matrix2D<ItemStackWeightTable> dcompScrap = new Matrix2D<ItemStackWeightTable>(ScrapValue.values().length, UPGRADE_NAMES.length);

	private static ItemStackItem getItemStackItem(final ItemStackWeightTable table, final Entry<String, Property> e) {
		ItemStackItem item = null;
		final int weight = e.getValue().getInt();
		final String key = e.getKey();
		
		if("destroy".equalsIgnoreCase(key))
			item = new ItemStackItem(null, weight);
		else if("debris".equalsIgnoreCase(key))
			item = new ItemStackItem(debris, weight);
		else if("keep".equalsIgnoreCase(key))
			item = new ItemStackItem(keep, weight);
		else if("dust".equalsIgnoreCase(key))
			item = new ItemStackItem(dust, weight);
		else if("poorScrap".equalsIgnoreCase(key))
			item = new ItemStackItem(poorScrap, weight);
		else if("standardScrap".equalsIgnoreCase(key))
			item = new ItemStackItem(standardScrap, weight);
		else if("superiorScrap".equalsIgnoreCase(key))
			item = new ItemStackItem(superiorScrap, weight);
		else {
			final ItemStack stack = ItemStackHelper.getItemStack(key);
			if(stack != null)
				item = new ItemStackItem(stack, weight);
		}

		return item;
	}
	
	private static void processTables(final String prefix, final Matrix2D<ItemStackWeightTable> weightTables, final JarConfiguration config) {
		for (final ScrapValue sv : ScrapValue.values())
			for (int i = 0; i < UPGRADE_NAMES.length; i++) {
				
				final String category = prefix + "_" + sv.name() + "_" + UPGRADE_NAMES[i];
				
				final ConfigCategory cc = config.getCategory(category);
				if(cc != null && !cc.isEmpty()) {
					
					final ItemStackWeightTable table = new ItemStackWeightTable();
					for(final Entry<String, Property> e: cc.getValues().entrySet()) {
						final ItemStackItem item = getItemStackItem(table, e);
						if(item != null)
							table.add(item);
					}
					weightTables.set(sv.ordinal(), i, table);
				}
			}
	}
	
	static {

		final InputStream in = ScrappingTables.class
				.getResourceAsStream("/assets/recycling/data/scrapdata.cfg");
		try {
			
			final JarConfiguration config = new JarConfiguration(in);
			processTables("dcomp", dcompScrap, config);
			//processTables("extract", extractDust, config);

			for(final ScrapValue sv: ScrapValue.values()) {
				final String category = sv.name() + "_MustScrap";
				final ItemStackWeightTable table = new ItemStackWeightTable();
				
				final ConfigCategory cc = config.getCategory(category);
				if(cc != null) {
					
					for(final Entry<String, Property> e: cc.getValues().entrySet()) {
						final ItemStackItem item = getItemStackItem(table, e);
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

	public static Optional<ItemStackWeightTable> getTable(final CoreType core, final ItemLevel level, final ScrapValue scrap) {
		
		assert core != CoreType.EXTRACTION;
		
		final int scrapOrdinal = scrap.ordinal();
		if(core == CoreType.NONE) {
			return Optional.fromNullable(mustScrap.get(scrapOrdinal));
		}
		
		final int levelOrdinal = level.ordinal();
		return dcompScrap.get(scrapOrdinal, levelOrdinal);
	}

	private static void dumpTable(final Writer writer, final String title,
			final List<ItemStackWeightTable> mustscrap2) throws Exception {

		for (int i = 0; i < mustscrap2.size(); i++) {
			final ItemStackWeightTable t = mustscrap2.get(i);
			if (t != null) {
				final String s = String.format("%s %s", title, UPGRADE_NAMES[i]);
				t.diagnostic(s, writer);
			}
		}
	}

	private static void dumpTable(final Writer writer, final String title,
			final Matrix2D<ItemStackWeightTable> table)
			throws Exception {
		
		for(int i = 0; i < table.getRowCount(); i++)
			for(int j = 0; j < table.getColCount(); j++) {
				if(table.isPresent(i, j)) {
					final Optional<ItemStackWeightTable> a = table.get(i, j);
					final String t = String.format("%s %s_%s", title, ScrapValue.values()[i].name(), UPGRADE_NAMES[j]);
					a.get().diagnostic(t, writer);
				}
			}
	}

	public static void writeDiagnostic(final Writer writer) throws Exception {

		writer.write("\n================\nScrap Tables\n================\n");
		dumpTable(writer, "Core: Decomposition", dcompScrap);
		dumpTable(writer, "Must Scrap", mustScrap);
	}
}

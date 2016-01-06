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

package org.blockartistry.mod.ThermalRecycling.items.scrapbox;

import java.util.Collections;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.util.EntityHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class EnchantedBookEffect extends UseEffectWeightTable.UseEffectItem {

	private static final int BOOK_TITLE_COUNT = 13;
	private static final List<String> titles;
	private static final List<List<String>> descriptions;
	
	static {

		final Builder<String> titleBuilder = ImmutableList.builder();
		final Builder<List<String>> descBuilder = ImmutableList.builder();
		
		for(int i = 0; i < BOOK_TITLE_COUNT; i++) {
			final String base = "msg.BookTitle" + i;
			titleBuilder.add(StatCollector.translateToLocal(base + ".name"));
			descBuilder.add(Collections.singletonList(StatCollector.translateToLocal(base + ".desc")));
		}
		
		titles = titleBuilder.build();
		descriptions = descBuilder.build();
	}
	
	private final Enchantment enchant;
	private final int level;

	public EnchantedBookEffect(final int weight, final int level) {
		this(weight, null, level);
	}

	public EnchantedBookEffect(final int weight, final Enchantment enchantment, final int level) {
		super(weight);
		this.enchant = enchantment;
		this.level = level;
	}

	@Override
	public void apply(final ItemStack scrap, final World world, final EntityPlayer player) {

		final ItemStack book = new ItemStack(Items.book);
		EnchantmentData[] enchants = null;

		if (this.enchant != null) {
			enchants = new EnchantmentData[1];
			enchants[0] = new EnchantmentData(this.enchant, this.level);
		} else {
			@SuppressWarnings("rawtypes")
			final List list = EnchantmentHelper
					.buildEnchantmentList(this.rnd, book, this.level);
			enchants = new EnchantmentData[list.size()];
			for (int i = 0; i < list.size(); i++)
				enchants[i] = (EnchantmentData) list.get(i);
		}

		// I guess this turns a book into an enchanted book
		book.func_150996_a(Items.enchanted_book);

		// Drop out one of the enchants if there are multiples. This is from
		// the enchanting table logic - maybe its to mitigate having too many
		// enchants floating around.
		final int j = enchants.length > 1 ? this.rnd.nextInt(enchants.length) : -1;

		// Attach the enchantments
		for (int i = 0; i < enchants.length; i++)
			if (i != j)
				Items.enchanted_book.addEnchantment(book, enchants[i]);
		
		final int index = this.rnd.nextInt(titles.size());

		ItemStackHelper.setItemName(book, titles.get(index));
		ItemStackHelper.setItemLore(book, descriptions.get(index));
		EntityHelper.spawnIntoWorld(book, world, player);
	}

	@Override
	public String toString() {
		return String.format("Enchanted Book [lvl %d]", this.level);
	}
}

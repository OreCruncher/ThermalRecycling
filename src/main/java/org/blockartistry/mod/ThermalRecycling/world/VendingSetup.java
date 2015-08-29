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

package org.blockartistry.mod.ThermalRecycling.world;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.util.FakePlayerHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public final class VendingSetup {

	private static Random random = XorShiftRandom.shared;

	private enum Profession {

		FARMER("msg.VendoFormat.Farmer", DyeHelper.COLOR_GREEN, DyeHelper.COLOR_WHITE),
		LIBRARIAN("msg.VendoFormat.Librarian", DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_BLUE),
		PRIEST("msg.VendoFormat.Priest", DyeHelper.COLOR_LIGHTGRAY, DyeHelper.COLOR_GRAY),
		BLACKSMITH("msg.VendoFormat.Blacksmith", DyeHelper.COLOR_BLACK, DyeHelper.COLOR_YELLOW),
		BUTCHER("msg.VendoFormat.Butcher", DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_RED),

		HERDER("msg.VendoFormat.Herder", DyeHelper.COLOR_BROWN, DyeHelper.COLOR_GREEN),
		ARBORIST("msg.VendoFormat.Arborist", DyeHelper.COLOR_GREEN, DyeHelper.COLOR_BROWN);

		public final String name;
		public final int foreColor;
		public final int backColor;

		private Profession(final String name, final int fColor, final int bColor) {
			this.name = StatCollector.translateToLocal(name);
			this.foreColor = fColor;
			this.backColor = bColor;
		}

		public static Profession randomProfession() {
			final int len = values().length;
			return values()[random.nextInt(len)];
		}
	}

	private static final String VENDO_FORMAT = StatCollector.translateToLocal("msg.VendoFormat");

	private static final Item[] AITEM = new Item[] { Items.iron_sword, Items.diamond_sword, Items.iron_chestplate,
			Items.diamond_chestplate, Items.iron_axe, Items.diamond_axe, Items.iron_pickaxe, Items.diamond_pickaxe };

	private VendingSetup() {
	}

	/**
	 * Configures a Vending Machine for village operation.
	 *
	 * @param vte
	 *            Vending TileEntity to configure
	 */
	public static void configure(final VendingTileEntity vte) {

		final ItemStack[] inv = vte.getRawInventory();
		final Profession profession = Profession.randomProfession();
		final int count = 2 + random.nextInt(5);

		// Use this method because if the spawn point is created before
		// the world is launched it is not possible to have a FakePlayer
		// "log in".
		vte.setOwnerId(FakePlayerHelper.getFakePlayerID());
		vte.setName(String.format(VENDO_FORMAT, profession.name));
		vte.setNameBackgroundColor(profession.backColor);
		vte.setNameColor(profession.foreColor);

		int index = 0;
		for (final MerchantRecipe rm : VendingSetup.getRecipeList(profession, count)) {
			if (rm != null) {
				final int base = index + VendingTileEntity.CONFIG_SLOT_START;
				inv[base] = rm.getItemToBuy().copy();
				if (rm.hasSecondItemToBuy())
					inv[base + 6] = rm.getSecondItemToBuy().copy();
				final ItemStack stack = inv[base + 12] = rm.getItemToSell().copy();

				// Put items in inventory to give to the player
				int stacks = 0;
				if (stack.isStackable())
					stacks = random.nextInt(6) + random.nextInt(6) + 2;
				else
					stacks = random.nextInt(3) + 1;

				for (int i = 0; i < stacks; i++)
					vte.addStackToOutput(stack.copy());

				index++;
			}
		}

	}

	private static float adjustProbability(final float prob, final float factor) {
		final float f1 = prob + factor;
		return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
	}

	@SuppressWarnings("unchecked")
	private static void addSaleItem(final MerchantRecipeList list, final ItemStack stack, final Random random,
			final float prob, final int minCost, final int maxCost) {
		if (random.nextFloat() < prob) {
			final int cost = random.nextInt(maxCost - minCost) + minCost;
			final ItemStack emeralds = new ItemStack(Items.emerald, cost);
			final MerchantRecipe recipe = new MerchantRecipe(emeralds, stack);
			list.add(recipe);
		}
	}

	@SuppressWarnings("unchecked")
	private static List<MerchantRecipe> getRecipeList(final Profession profession, final int count) {

		final MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
		final float factor = MathHelper.sqrt_float((float) count) * 0.2F;
		int k = 0;

		label50:

		switch (profession) {
		case FARMER:
			EntityVillager.func_146091_a(merchantrecipelist, Items.wheat, random, adjustProbability(0.9F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Item.getItemFromBlock(Blocks.wool), random,
					adjustProbability(0.5F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.chicken, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.cooked_fished, random,
					adjustProbability(0.4F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.bread, random, adjustProbability(0.9F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.melon, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.apple, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.cookie, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.shears, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.flint_and_steel, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.cooked_chicken, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.arrow, random, adjustProbability(0.5F, factor));

			if (random.nextFloat() < adjustProbability(0.5F, factor)) {
				merchantrecipelist.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10),
						new ItemStack(Items.emerald), new ItemStack(Items.flint, 4 + random.nextInt(2), 0)));
			}

			break;
		case LIBRARIAN:
			EntityVillager.func_146091_a(merchantrecipelist, Items.paper, random, adjustProbability(0.8F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.book, random, adjustProbability(0.8F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.written_book, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.bookshelf), random,
					adjustProbability(0.8F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.glass), random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.compass, random, adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.clock, random, adjustProbability(0.2F, factor));

			if (random.nextFloat() < adjustProbability(0.07F, factor)) {
				Enchantment enchantment = Enchantment.enchantmentsBookList[random
						.nextInt(Enchantment.enchantmentsBookList.length)];
				int i1 = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(),
						enchantment.getMaxLevel());
				ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i1));
				k = 2 + random.nextInt(5 + i1 * 10) + 3 * i1;
				merchantrecipelist
						.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, k), itemstack));
			}

			break;
		case PRIEST:
			EntityVillager.func_146089_b(merchantrecipelist, Items.ender_eye, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.experience_bottle, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.redstone, random, adjustProbability(0.4F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.glowstone), random,
					adjustProbability(0.3F, factor));

			int j = AITEM.length;
			k = 0;

			while (true) {
				if (k >= j) {
					break label50;
				}

				Item item = AITEM[k];

				if (random.nextFloat() < adjustProbability(0.05F, factor)) {
					merchantrecipelist.add(new MerchantRecipe(new ItemStack(item, 1, 0),
							new ItemStack(Items.emerald, 2 + random.nextInt(3), 0), EnchantmentHelper
									.addRandomEnchantment(random, new ItemStack(item, 1, 0), 5 + random.nextInt(15))));
				}

				++k;
			}
		case BLACKSMITH:
			EntityVillager.func_146091_a(merchantrecipelist, Items.coal, random, adjustProbability(0.7F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.iron_ingot, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.gold_ingot, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.diamond, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_sword, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_sword, random,
					adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_axe, random, adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_axe, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_pickaxe, random,
					adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_pickaxe, random,
					adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_shovel, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_shovel, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_hoe, random, adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_hoe, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_boots, random, adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_boots, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_helmet, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_helmet, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_chestplate, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_chestplate, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.iron_leggings, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.diamond_leggings, random,
					adjustProbability(0.2F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.chainmail_boots, random,
					adjustProbability(0.1F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.chainmail_helmet, random,
					adjustProbability(0.1F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.chainmail_chestplate, random,
					adjustProbability(0.1F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.chainmail_leggings, random,
					adjustProbability(0.1F, factor));
			break;
		case HERDER:
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 90), random,
					adjustProbability(0.5F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 91), random,
					adjustProbability(0.5F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 92), random,
					adjustProbability(0.5F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 93), random,
					adjustProbability(0.5F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 94), random,
					adjustProbability(0.2F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 95), random,
					adjustProbability(0.2F, factor), 2, 4);
			addSaleItem(merchantrecipelist, new ItemStack(Items.spawn_egg, 1, 100), random,
					adjustProbability(0.1F, factor), 4, 7);
			break;
		case ARBORIST:
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log, 32, 0), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log, 32, 1), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log, 32, 2), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log, 32, 3), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log2, 32, 0), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.log2, 32, 1), random,
					adjustProbability(0.5F, factor), 1, 2);
			
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 0), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 1), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 2), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 3), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 4), random,
					adjustProbability(0.5F, factor), 1, 2);
			addSaleItem(merchantrecipelist, new ItemStack(Blocks.sapling, 4, 5), random,
					adjustProbability(0.5F, factor), 1, 2);
			break;
		case BUTCHER:
		default:
			EntityVillager.func_146091_a(merchantrecipelist, Items.coal, random, adjustProbability(0.7F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.porkchop, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146091_a(merchantrecipelist, Items.beef, random, adjustProbability(0.5F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.saddle, random, adjustProbability(0.1F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.leather_chestplate, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.leather_boots, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.leather_helmet, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.leather_leggings, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.cooked_porkchop, random,
					adjustProbability(0.3F, factor));
			EntityVillager.func_146089_b(merchantrecipelist, Items.cooked_beef, random,
					adjustProbability(0.3F, factor));
		}

		if (merchantrecipelist.isEmpty()) {
			EntityVillager.func_146091_a(merchantrecipelist, Items.gold_ingot, random, 1.0F);
		}

		Collections.shuffle(merchantrecipelist);

		final MerchantRecipeList result = new MerchantRecipeList();

		for (int l = 0; l < count && l < merchantrecipelist.size(); ++l) {
			if (profession == Profession.HERDER) {
				result.add(merchantrecipelist.get(l));
			} else {
				result.addToListWithCheck((MerchantRecipe) merchantrecipelist.get(l));
			}
		}

		return result;
	}

}

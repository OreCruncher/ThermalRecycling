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

package org.blockartistry.mod.ThermalRecycling.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public final class PaperLogMaker extends ItemBase {

	private static final int MAX_DAMAGE = 128;

	public PaperLogMaker() {
		super("paperlogmaker");

		setUnlocalizedName("PaperLogMaker");
		setMaxStackSize(1);
		setMaxDamage(MAX_DAMAGE);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(final ItemStack itemstack) {
		// Leave in the grid for shift click
		return false;
	}

	@Override
	public ItemStack getContainerItem(final ItemStack itemStack) {
		final ItemStack stack = itemStack.copy();
		stack.setItemDamage(ItemStackHelper.getItemDamage(stack) + 1);
		return stack;
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return ItemStackHelper.getItemDamage(stack) < MAX_DAMAGE - 1;
	}

	@Override
	public IIcon getIconFromDamage(final int subType) {
		return icons[0];
	}

	@Override
	public void register() {
		super.register();

		final ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(
				ItemManager.paperLogMaker), "s  ", "sss", "w w", 's',
				new ItemStack(Items.stick, 1, OreDictionaryHelper.WILDCARD_VALUE),
				'w', new ItemStack(Blocks.wooden_slab, 1,
						OreDictionaryHelper.WILDCARD_VALUE));

		GameRegistry.addRecipe(recipe);
	}
}

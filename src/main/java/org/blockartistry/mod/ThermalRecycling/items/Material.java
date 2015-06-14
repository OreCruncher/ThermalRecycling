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

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import cpw.mods.fml.common.registry.GameRegistry;

public final class Material extends ItemBase {

	private static final Random random = XorShiftRandom.shared;
	private static final int MIN_EGGS_TO_SPAWN = 0;
	private static final int SPREAD_EGGS_TO_SPAWN = 4;

	public static final int PAPER_LOG = 0;
	public static final int WORMS = 1;

	public Material() {
		super("paperlog", "worms");

		setUnlocalizedName("Material");
		setHasSubtypes(true);
		setMaxStackSize(64);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack,
			EntityPlayer player, EntityLivingBase target) {

		if (target.worldObj.isRemote || stack.getItemDamage() != WORMS)
			return false;

		if (target instanceof EntityChicken) {

			stack.stackSize--;

			final ItemStack eggs = new ItemStack(Items.egg, MIN_EGGS_TO_SPAWN
					+ random.nextInt(SPREAD_EGGS_TO_SPAWN));
			ItemStackHelper.spawnIntoWorld(target.worldObj, eggs, target.posX,
					target.posY, target.posZ);

			return true;
		}

		return false;
	}

	@Override
	public void register() {
		super.register();

		final ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(
				ItemManager.material, 1, PAPER_LOG), "ppp", "plp", "ppp", 'p',
				new ItemStack(Items.paper), 'l', new ItemStack(
						ItemManager.paperLogMaker, 1,
						OreDictionary.WILDCARD_VALUE));

		GameRegistry.addRecipe(recipe);
	}
}

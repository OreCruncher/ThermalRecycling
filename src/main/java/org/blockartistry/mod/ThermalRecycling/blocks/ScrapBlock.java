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

package org.blockartistry.mod.ThermalRecycling.blocks;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.util.MultiBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public final class ScrapBlock extends MultiBlock {

	public ScrapBlock() {
		super("ScrapBlock", Material.ground, 3, "scrap");

		setHarvestLevel("pickaxe", 1);
		setHarvestLevel("shovel", 1);
		setHardness(0.5F);
		setStepSound(Block.soundTypeGravel);
	}

	@Override
	public void register() {
		GameRegistry.registerBlock(this, ScrapItemBlock.class,
				myUnlocalizedName);

		ItemStack debris = new ItemStack(ItemManager.debris);

		GameRegistry.addShapelessRecipe(new ItemStack(BlockManager.scrapBlock),
				debris, debris, debris, debris, debris, debris, debris, debris,
				debris);

	}

	@Override
	public String[] getBlockSideTextures(int subType) {
		String texture = ThermalRecycling.MOD_ID + ":scrap_block";
		return new String[] { texture, texture, texture };
	}

}

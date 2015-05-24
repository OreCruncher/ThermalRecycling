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

package org.blockartistry.mod.ThermalRecycling.support.handlers;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public final class ForestryFarmScrapHandler extends GenericHandler {
	
	// From Forestry code.  Used to crack the underlying style
	// of block used to make a farm block.
	protected enum EnumFarmBlock {
		BRICK_STONE(new ItemStack(Blocks.stonebrick, 1, 0)),
		BRICK_MOSSY(new ItemStack(Blocks.stonebrick, 1, 1)),
		BRICK_CRACKED(new ItemStack(Blocks.stonebrick, 1, 2)),
		BRICK(new ItemStack(Blocks.brick_block)),
		SANDSTONE_SMOOTH(new ItemStack(Blocks.sandstone, 1, 2)),
		SANDSTONE_CHISELED(new ItemStack(Blocks.sandstone, 1, 1)),
		BRICK_NETHER(new ItemStack(Blocks.nether_brick)),
		BRICK_CHISELED(new ItemStack(Blocks.stonebrick, 1, 3)),
		QUARTZ(new ItemStack(Blocks.quartz_block, 1, 0)),
		QUARTZ_CHISELED(new ItemStack(Blocks.quartz_block, 1, 1)),
		QUARTZ_LINES(new ItemStack(Blocks.quartz_block, 1, 2));

		public final ItemStack base;

		EnumFarmBlock(ItemStack base) {
			this.base = base;
		}

		public void saveToCompound(NBTTagCompound compound) {
			compound.setInteger("FarmBlock", this.ordinal());
		}
		
		public static EnumFarmBlock getFromCompound(NBTTagCompound compound) {

			if (compound != null) {
				int farmBlockOrdinal = compound.getInteger("FarmBlock");
				if (farmBlockOrdinal < EnumFarmBlock.values().length) {
					return EnumFarmBlock.values()[farmBlockOrdinal];
				}
			}

			return null;
		}
	}

	static final ItemStack sample;
	static final ItemStack[] farmBlocks;
	
	static {
		
		sample = ItemStackHelper.getItemStack("Forestry:ffarm:0");
		farmBlocks = new ItemStack[EnumFarmBlock.values().length];
		for(EnumFarmBlock fb: EnumFarmBlock.values()) {
			ItemStack block = sample.copy();
			NBTTagCompound nbt = new NBTTagCompound();
			fb.saveToCompound(nbt);
			block.setTagCompound(nbt);
			farmBlocks[fb.ordinal()] = block;
		}
	};

	@Override
	protected List<ItemStack> getRecipeOutput(ItemStack stack) {
		
		List<ItemStack> result = super.getRecipeOutput(stack);
		
		if(stack.hasTagCompound() && stack.getItemDamage() > 0) {
			NBTTagCompound nbt = stack.getTagCompound();
			EnumFarmBlock blockStyle = EnumFarmBlock.getFromCompound(nbt);
			if(blockStyle != null) {
				// Find the target block to replace
				for(int i = 0; i < result.size(); i++) {
					ItemStack subject = result.get(i);
					if(subject.isItemEqual(sample)) {
						ItemStack replace = farmBlocks[blockStyle.ordinal()].copy();
						replace.stackSize = subject.stackSize;
						result.set(i, replace);
					}
				}
			}
		}
		
		return result;
	}
}

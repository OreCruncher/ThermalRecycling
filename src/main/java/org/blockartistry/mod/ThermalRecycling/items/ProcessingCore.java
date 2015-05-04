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
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class ProcessingCore extends ItemBase {

	public static final int DECOMPOSITION = 0;
	public static final int EXTRACTION = 1;

	private static final String[] types = new String[] { "decomposition",
			"extraction" };

	public ProcessingCore() {
		super(types);

		setUnlocalizedName("ProcessingCore");
		setMaxStackSize(1);
	}

	@Override
	public void register() {
		super.register();

		GameRegistry.addShapedRecipe(new ItemStack(ItemManager.processingCore,
				1, DECOMPOSITION), " h ", "mMm", "tst", 'h', new ItemStack(
				Blocks.hopper), 'm', ItemStackHelper
				.getItemStack("ThermalExpansion:meter"), 'M', ItemStackHelper
				.getItemStack("ThermalExpansion:Frame"), 't', ItemStackHelper
				.getItemStack("gearTin"), 's', ItemStackHelper
				.getItemStack("ThermalExpansion:material"));
	}
}

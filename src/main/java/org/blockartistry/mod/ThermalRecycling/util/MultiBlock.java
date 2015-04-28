/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.ThermalRecycling.util;

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.CreativeTabManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import com.google.common.base.Preconditions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public abstract class MultiBlock extends Block {

	/**
	 * A list of internal names for all the subtypes implemented by the derived
	 * item implementation. An item with no subtypes will have a single entry,
	 * whereas an item with subtypes will have multiples.
	 */
	public final String[] names;
	public final String myUnlocalizedName;

	@SideOnly(Side.CLIENT)
	protected IIcon[][] icons;

	public MultiBlock(String name, Material material, int sides, String... n) {
		super(material);

		Preconditions.checkArgument(n != null && n.length > 0);

		names = n;
		myUnlocalizedName = name;
		icons = new IIcon[names.length][sides];

		setBlockName(name);
		setCreativeTab(CreativeTabManager.tab);
	}

	/**
	 * Gets the block's texture. Args: side, meta. Sides: Bottom (0), Top (1),
	 * North (2), South (3), West (4), East (5)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {

		int subType = MathHelper.clamp_int(meta, 0, icons.length - 1);
		IIcon[] i = icons[subType];

		int s = MathHelper.clamp_int(side, 0, i.length - 1);
		return i[s];
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab,
			@SuppressWarnings("rawtypes") List subItems) {
		for (int i = 0; i < names.length; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	public String[] getBlockSideTextures(int subType) {

		ArrayList<String> textures = new ArrayList<String>();
		int sides = icons[subType].length;

		String s = ThermalRecycling.MOD_ID + ":" + myUnlocalizedName + "_"
				+ names[subType];

		if (sides == 1) {
			textures.add(s);
		} else {
			for (int i = 0; i < sides; i++) {
				textures.add(s + "_" + BlockSide.lookup[i].sideName);
			}
		}

		return (String[]) textures.toArray();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		for (int i = 0; i < names.length; i++) {
			String[] textures = getBlockSideTextures(i);
			for (int j = 0; j < textures.length; j++) {
				icons[i][j] = iconRegister.registerIcon(textures[j]);
			}
		}
	}

	public abstract void register();

}

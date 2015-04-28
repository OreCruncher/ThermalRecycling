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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.CreativeTabManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import com.google.common.base.Preconditions;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

/**
 * A generic class used to fill out and handle boilerplate that is associated
 * with Minecraft items.
 *
 */
public abstract class ItemBase extends Item {

	/**
	 * A list of internal names for all the subtypes implemented by the derived
	 * item implementation. An item with no subtypes will have a single entry,
	 * whereas an item with subtypes will have multiples.
	 */
	protected final String[] names;

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	protected String myUnlocalizedName;

	/**
	 * The CTOR that is to be used by the deriving implementation. It takes one
	 * or more item names that will be handled by the item implementation. It
	 * will automatically set subType information based on the length of the
	 * list provided.
	 * 
	 * It should be obvious, but the ordering of the incoming names are
	 * important. The first item is subtype 0, second subtype 1, etc.
	 * 
	 * @param n
	 */
	public ItemBase(String... n) {

		super();

		Preconditions.checkArgument(n != null && n.length > 0);

		names = n;
		icons = new IIcon[names.length];

		setHasSubtypes(names.length > 1);
		setCreativeTab(CreativeTabManager.tab);
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			@SuppressWarnings("rawtypes") List par3List) {
		for (int x = 0; x < names.length; x++) {
			par3List.add(new ItemStack(this, 1, x));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		for (int i = 0; i < icons.length; i++) {
			icons[i] = iconRegister.registerIcon(ThermalRecycling.MOD_ID + ":"
					+ myUnlocalizedName + i);
		}
	}

	@Override
	public IIcon getIconFromDamage(int subType) {
		return icons[subType];
	}

	@Override
	public Item setUnlocalizedName(String name) {

		super.setUnlocalizedName(name);
		myUnlocalizedName = name;
		return this;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int i = MathHelper
				.clamp_int(stack.getItemDamage(), 0, names.length - 1);
		return getUnlocalizedName() + "." + names[i];
	}

	/**
	 * This method is invoke by the framework to register the item with Forge.
	 */
	public final void register() {
		GameRegistry.registerItem(this, myUnlocalizedName);
	}
}

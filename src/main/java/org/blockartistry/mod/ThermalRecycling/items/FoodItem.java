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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class FoodItem extends ItemFood {

	public static abstract class OnEat {
		public abstract void onEat(final ItemStack stack, final World world, final EntityPlayer player);
	}
	
	private String myUnlocalizedName;
	private String lore = null;
	private OnEat eaten = null;
	

	@SideOnly(Side.CLIENT)
	protected IIcon icon;

	public FoodItem(final String unlocalizedName, final int healAmount,
			final float saturation, final boolean wolfFood) {
		super(healAmount, saturation, wolfFood);

		setUnlocalizedName(unlocalizedName);
		setMaxStackSize(64);
	}

	public FoodItem(final String unlocalizedName, final int healAmount,
			final boolean wolfFood) {
		this(unlocalizedName, healAmount, 0.6F, wolfFood);
	}

	public FoodItem setOnEaten(final OnEat eaten) {
		this.eaten = eaten;
		return this;
	}
	
	public FoodItem setLore(final String lore) {
		this.lore = lore;
		return this;
	}

	@Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		if(eaten != null)
			eaten.onEat(stack, world, player);
		return super.onEaten(stack, world, player);
    }
    
	@Override
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack,
			final EntityPlayer player,
			@SuppressWarnings("rawtypes") final List info,
			final boolean p_77624_4_) {

		if (lore != null && !lore.isEmpty()) {
			info.add(lore);
		}
	}

	@Override
	public Item setUnlocalizedName(final String name) {
		super.setUnlocalizedName(name);
		myUnlocalizedName = name;
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(final IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ThermalRecycling.MOD_ID + ":"
				+ myUnlocalizedName);
	}

	@Override
	public IIcon getIconFromDamage(final int subType) {
		return icon;
	}

	public void register() {
		GameRegistry.registerItem(this, myUnlocalizedName);
	}
}

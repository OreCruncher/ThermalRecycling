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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.blockartistry.mod.ThermalRecycling.AchievementManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.scrapbox.UseEffect;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RecyclingScrapBox extends ItemBase {
	
	public static final int POOR = 0;
	public static final int STANDARD = 1;
	public static final int SUPERIOR = 2;

	private static final String[] types = new String[] { "poor", "standard",
			"superior" };

	public RecyclingScrapBox() {
		super(types);

		setUnlocalizedName("RecyclingScrapBox");
		setMaxStackSize(64);
	}
	
    @SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List info, boolean p_77624_4_) {
    	info.add(StatCollector.translateToLocal(String.format("%s.%s.desc", getUnlocalizedName(), types[stack.getItemDamage()])));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
    	return stack.getItemDamage() == SUPERIOR;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	if(!world.isRemote) {
    		UseEffect.triggerEffect(stack, world, player);
    		player.addStat(AchievementManager.doingMyPart, 1);
    	}
    	
    	return stack;
    }
    
	public void register() {
		super.register();
		
		// Make the recipes here
		for(int i = 0; i < 4; i++) {
			GameRegistry.addShapedRecipe(new ItemStack(ItemManager.recyclingScrapBox, 1, i), "sss", "s s", "sss", 's', new ItemStack(ItemManager.recyclingScrap, 1, i));
		}
	}
}
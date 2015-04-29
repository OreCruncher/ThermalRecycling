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

package org.blockartistry.mod.ThermalRecycling.items.scrapbox;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

public class EnchantedBookEffect extends UseEffect {
	
	Enchantment enchant;
	int level;

	public EnchantedBookEffect(int level) {
		this(null, level);
	}

	public EnchantedBookEffect(Enchantment enchantment, int level) {
		this.enchant = enchantment;
		this.level = level;
	}
	
	protected void addLore(ItemStack stack) {
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) nbt = new NBTTagCompound();

		NBTTagList lore = new NBTTagList();
		lore.appendTag(new NBTTagString("Reduce! Reuse! Recycle!"));
		NBTTagCompound display = new NBTTagCompound();
		display.setTag("Lore", lore);

		nbt.setTag("display", display);
		stack.setTagCompound(nbt);		
	}
	
	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {

		ItemStack book = new ItemStack(Items.book);
		EnchantmentData[] enchants = null;
		
		if(enchant != null) {
			enchants = new EnchantmentData[1];
			enchants[0] = new EnchantmentData(enchant, level);
		}
		else {
			@SuppressWarnings("rawtypes")
			List list = EnchantmentHelper.buildEnchantmentList(rand, book, level);
			enchants = new EnchantmentData[list.size()];
			for(int i = 0; i < list.size(); i++)
				enchants[i] = (EnchantmentData)list.get(i);
		}
		
		// I guess this turns a book into an enchanted book
        book.func_150996_a(Items.enchanted_book);

        // Drop out one of the enchants if there are multiples.  This is from
        // the enchanting table logic - maybe its to mitigate having too many
        // enchants floating around.
        int j = enchants.length > 1 ? rand.nextInt(enchants.length) : -1;
        
        // Attach the enchantments
        for(int i = 0; i < enchants.length; i++)
        	if(i != j)
        		Items.enchanted_book.addEnchantment(book, enchants[i]);
		
        addLore(book);
		spawnIntoWorld(book, world, player);
	}
	
	@Override
	public String toString() {
		return String.format("Enchanted Book [%d]", level);
	}
}

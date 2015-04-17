/*
 * This file is part of ModpackInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 * Copyright (c) contributors
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

package org.blockartistry.mod.ThermalRecycling.support;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

public class ModThermalFoundation extends ModTweaks {

	protected static ItemStack pyrotheumDust;

	protected ItemStack getPyrotheumDust(int quantity) {

		if (pyrotheumDust == null) {
			pyrotheumDust = RecipeHelper.getItemStack(
					"ThermalFoundation:material", 1);
			pyrotheumDust.setItemDamage(512);
		}

		ItemStack result = pyrotheumDust.copy();
		result.stackSize = quantity;
		return result;
	}

	protected void furnaceRecycleHelperTE(String oreName) {

		RecipeHelper helper = new RecipeHelper();

		helper.setOutput("ingot" + oreName, 1);

		helper.setInput("ThermalFoundation:armor.helmet" + oreName, 1)
				.setOutputQuantity(5).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:armor.plate" + oreName, 1)
				.setOutputQuantity(8).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:armor.legs" + oreName, 1)
				.setOutputQuantity(7).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:armor.boots" + oreName, 1)
				.setOutputQuantity(4).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.sword" + oreName, 1)
				.setOutputQuantity(2).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.shovel" + oreName, 1)
				.setOutputQuantity(1).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.pickaxe" + oreName, 1)
				.setOutputQuantity(3).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.axe" + oreName, 1)
				.setOutputQuantity(3).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.hoe" + oreName, 1)
				.setOutputQuantity(2).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.shears" + oreName, 1)
				.setOutputQuantity(2).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.fishingRod" + oreName, 1)
				.setOutputQuantity(2).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.sickle" + oreName, 1)
				.setOutputQuantity(3).addAsFurnaceRecipe();
		helper.setInput("ThermalFoundation:tool.bow" + oreName, 1)
				.setOutputQuantity(2).addAsFurnaceRecipe();
	}

	protected void recycleGearTE(String type) {

		RecipeHelper helper = new RecipeHelper();

		helper.setInput("gear" + type, 1).setOutput("ingot" + type, 4)
				.addAsFurnaceRecipe();
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();

		// Big daddy golden apple
		ItemStack apple = new ItemStack(Items.golden_apple, 1, 1);
		ItemStack pyro = getPyrotheumDust(8);
		helper.reset().setEnergy(72000);
		helper.setInput(apple).setSecondary(pyro).setOutput("blockGold", 8)
				.addAsSmelterRecipe();

		// Smelt some blocks!
		helper.setEnergy(21600);
		helper.setInput("blockTin").setSecondary("blockCopper", 3)
				.setOutput("blockBronze", 4).addAsSmelterRecipe();
		helper.setInput("blockGold").setSecondary("blockSilver")
				.setOutput("blockElectrum", 2).addAsSmelterRecipe();
		helper.setInput("blockIron", 2).setSecondary("blockNickel")
				.setOutput("blockInvar", 3).addAsSmelterRecipe();

		furnaceRecycleHelperTE("Copper");
		furnaceRecycleHelperTE("Tin");
		furnaceRecycleHelperTE("Silver");
		furnaceRecycleHelperTE("Lead");
		furnaceRecycleHelperTE("Nickel");
		furnaceRecycleHelperTE("Electrum");
		furnaceRecycleHelperTE("Invar");
		furnaceRecycleHelperTE("Bronze");
		furnaceRecycleHelperTE("Platinum");

		recycleGearTE("Iron");
		recycleGearTE("Gold");
		recycleGearTE("Copper");
		recycleGearTE("Tin");
		recycleGearTE("Silver");
		recycleGearTE("Lead");
		recycleGearTE("Nickel");
		recycleGearTE("Platinum");
		recycleGearTE("Electrum");
		recycleGearTE("Invar");
		recycleGearTE("Bronze");
		recycleGearTE("Signalum");
		recycleGearTE("Lumium");
		recycleGearTE("Enderium");

	}
}

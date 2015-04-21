package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.item.ItemStack;
import cofh.api.modhelpers.ThermalExpansionHelper;

public class FurnaceRecipeBuilder extends RecipeBuilder {

	@Override
	protected boolean saveImpl(ItemStack stack) {

		if (output != null) {
			ThermalExpansionHelper.addFurnaceRecipe(energy, stack, output);
			return true;
		}

		return false;
	}

	@Override
	protected String toString(ItemStack in) {

		return String.format("Redstone Furnace [%dx %s] => [%dx %s]",
				in.stackSize, resolveName(in), output.stackSize,
				resolveName(output));
	}
}

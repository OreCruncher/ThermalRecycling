package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.item.ItemStack;
import cofh.api.modhelpers.ThermalExpansionHelper;

public class SmelterRecipeBuilder extends SecondaryInputRecipeBuilder {

	@Override
	protected boolean saveImpl(ItemStack stack) {

		if (output != null) {
			ThermalExpansionHelper.addSmelterRecipe(energy, stack,
					secondaryInput, output);
			return true;
		}

		return false;
	}

	@Override
	protected String toString(ItemStack in) {

		return String.format("Induction Smelter [%dx %s, %dx %s] => [%dx %s]",
				in.stackSize, resolveName(in), secondaryInput.stackSize,
				resolveName(secondaryInput), output.stackSize,
				resolveName(output));
	}
}

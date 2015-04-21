package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.item.ItemStack;
import cofh.api.modhelpers.ThermalExpansionHelper;

public class PulverizerRecipeBuilder extends SecondaryOutputRecipeBuilder {

	@Override
	protected boolean saveImpl(ItemStack stack) {

		if (output != null) {
			ThermalExpansionHelper.addPulverizerRecipe(energy, stack, output,
					secondaryOutput, secondaryChance);
			return true;
		}

		return false;
	}

	@Override
	protected String toString(ItemStack in) {

		StringBuilder builder = new StringBuilder();

		builder.append(String.format("Pulverizer [%dx %s] => [%dx %s",
				in.stackSize, resolveName(in), output.stackSize,
				resolveName(output)));

		if (secondaryOutput != null) {
			builder.append(String.format(", %dx %s @%d",
					secondaryOutput.stackSize, resolveName(secondaryOutput),
					secondaryChance));
		}

		builder.append("]");

		return builder.toString();
	}
}

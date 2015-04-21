package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.modhelpers.ThermalExpansionHelper;

public class FluidTransposerRecipeBuilder extends RecipeBuilder {

	protected FluidStack fluid;

	public FluidTransposerRecipeBuilder() {
		fluid = null;
	}

	@Override
	public RecipeBuilder reset() {
		fluid = null;
		return super.reset();
	}

	@Override
	public RecipeBuilder fluid(String fluidId) {
		fluid(RecipeHelper.getFluidStack(fluidId, 1000));
		return this;
	}

	@Override
	public RecipeBuilder fluid(String fluidId, int quantity) {
		fluid(RecipeHelper.getFluidStack(fluidId, quantity));
		return this;
	}

	@Override
	public RecipeBuilder fluid(FluidStack stack) {
		this.fluid = stack;
		return this;
	}

	@Override
	public RecipeBuilder fluidQuantity(int quantity) {
		if (this.fluid != null)
			this.fluid.amount = quantity;
		return this;
	}

	@Override
	protected boolean saveImpl(ItemStack stack) {
		if (output != null && fluid != null) {
			ThermalExpansionHelper.addTransposerExtract(energy, stack, output,
					fluid, 100, false);

			return true;
		}

		return false;
	}

	@Override
	protected String toString(ItemStack in) {

		return String.format("Fluid Transposer [%dx %s] => [%dx %s, %dmb %s]",
				in.stackSize, resolveName(in), output.stackSize,
				resolveName(output), fluid.amount, fluid.getLocalizedName());
	}
}

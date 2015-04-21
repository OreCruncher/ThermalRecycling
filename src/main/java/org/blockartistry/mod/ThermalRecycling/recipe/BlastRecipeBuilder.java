package org.blockartistry.mod.ThermalRecycling.recipe;

import org.blockartistry.mod.ThermalRecycling.support.ModThermalFoundation;

public class BlastRecipeBuilder extends SmelterRecipeBuilder {

	@Override
	public void save() {

		if (energy < 8000)
			energy = 8000;
		secondaryInput = ModThermalFoundation.getPyrotheumDust(1);
		super.save();
	}
}

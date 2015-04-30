package org.blockartistry.mod.ThermalRecycling.support;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class ModThermalRecycling extends ModPlugin {

	public ModThermalRecycling() {
		super(SupportedMod.THERMAL_RECYCLING);
	}

	@Override
	public void apply() {
		
		//////////////////////
		//
		// Add recipe blacklist items first
		// before processing!
		//
		//////////////////////
		
		RecipeBlacklist.add(ItemManager.recyclingScrapBox);
		
		//////////////////////
		//
		// Process the recipes
		//
		//////////////////////
		
		// Process all registered recipes
		for(Object o: CraftingManager.getInstance().getRecipeList()) {
			
			IRecipe recipe = (IRecipe)o;
			ItemStack stack = recipe.getRecipeOutput();
			
			// Check to see if this item should have a recipe in
			// the list.  This does not mean that something later
			// on can't add one - just means by default it will
			// not be included.
			if(stack != null) {
				if(!RecipeBlacklist.isListed(stack)) {
					recycler.useRecipe(stack).save();
				}
			}
		}
	}
}

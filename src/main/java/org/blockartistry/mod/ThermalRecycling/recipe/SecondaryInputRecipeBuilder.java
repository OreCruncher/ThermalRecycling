package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class SecondaryInputRecipeBuilder extends RecipeBuilder {

	protected ItemStack secondaryInput;

	public SecondaryInputRecipeBuilder() {
		secondaryInput = null;
	}

	@Override
	public RecipeBuilder reset() {
		secondaryInput = null;
		return super.reset();
	}

	/*
	 * public RecipeBuilder resetSecondaryInput() { this.secondaryInput = null;
	 * return this; }
	 */

	@Override
	public RecipeBuilder secondaryInput(Block block) {
		return secondaryInput(block, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(Block block, int quantity) {
		return secondaryInput(new ItemStack(block, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(Item item) {
		return secondaryInput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(Item item, int quantity) {
		return secondaryInput(new ItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(String item) {
		return secondaryInput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(String item, int quantity) {
		return secondaryInput(RecipeHelper.getItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(ItemStack sec) {
		this.secondaryInput = sec;
		return this;
	}

	@Override
	public RecipeBuilder secondaryInputSubtype(int type) {
		if (this.secondaryInput != null)
			this.secondaryInput.setItemDamage(type);
		return this;
	}

	public RecipeBuilder setSecondaryInputQuantity(int quantity) {
		if (this.secondaryInput != null)
			this.secondaryInput.stackSize = quantity;
		return this;
	}
}

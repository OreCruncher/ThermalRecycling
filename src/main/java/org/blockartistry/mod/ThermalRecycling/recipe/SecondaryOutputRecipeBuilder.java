package org.blockartistry.mod.ThermalRecycling.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class SecondaryOutputRecipeBuilder extends RecipeBuilder {

	protected ItemStack secondaryOutput;
	protected int secondaryChance;

	public SecondaryOutputRecipeBuilder() {
		secondaryOutput = null;
		secondaryChance = 100;
	}

	@Override
	public RecipeBuilder reset() {
		secondaryOutput = null;
		secondaryChance = 100;
		return super.reset();
	}

	/*
	 * public RecipeBuilder resetSecondary() { this.secondary = null; return
	 * this; }
	 */

	@Override
	public RecipeBuilder secondaryOutput(Block block) {
		return secondaryOutput(block, 1);
	}

	@Override
	public RecipeBuilder secondaryOutput(Block block, int quantity) {
		return secondaryOutput(new ItemStack(block, quantity));
	}

	@Override
	public RecipeBuilder secondaryOutput(Item item) {
		return secondaryOutput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryOutput(Item item, int quantity) {
		return secondaryOutput(new ItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryOutput(String item) {
		return secondaryOutput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryOutput(String item, int quantity) {
		return secondaryOutput(RecipeHelper.getItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryOutput(ItemStack sec) {
		this.secondaryOutput = sec;
		return this;
	}

	@Override
	public RecipeBuilder secondaryOutputSubtype(int type) {
		if (this.secondaryOutput != null)
			this.secondaryOutput.setItemDamage(type);
		return this;
	}

	@Override
	public RecipeBuilder secondaryOutputQuantity(int quantity) {
		if (this.secondaryOutput != null)
			this.secondaryOutput.stackSize = quantity;
		return this;
	}

	@Override
	public RecipeBuilder chance(int chance) {
		this.secondaryChance = chance;
		return this;
	}
}

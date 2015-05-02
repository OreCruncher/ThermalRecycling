package org.blockartistry.mod.ThermalRecycling.support.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeDecomposition implements Iterable<ItemStack> {

	static final String[] classIgnoreList = new String[] {
		"forestry.lepidopterology.MatingRecipe",
		"cofh.thermaldynamics.util.crafting.RecipeCover",
		"mods.railcraft.common.carts.LocomotivePaintingRecipe",
		"mods.railcraft.common.emblems.EmblemPostColorRecipe"
	};
	
	public class MyIterator<T> implements Iterator<T> {

		ItemStack[] list;
		int index;

		public MyIterator(ItemStack[] list) {
			this.list = list;
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return list != null && index < list.length;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T next() {
			return (T) list[index++];
		}

		@Override
		public void remove() {
			// implement... if supported.
		}
	}

	static Field teRecipeAccessor = null;
	static Field forestryRecipeAccessor = null;

	final IRecipe recipe;
	ItemStack[] projection;
	ItemStack inputStack;

	public RecipeDecomposition(ItemStack input, Object... output) {
		inputStack = input;
		recipe = null;
		projection = projectForgeRecipeList(output);
		scrubProjection();
	}
	
	public RecipeDecomposition(IRecipe recipe) {
		this.recipe = recipe;
	}

	public ItemStack getInput() {
		return inputStack != null ? inputStack.copy() : recipe.getRecipeOutput().copy();
	}
	
	public ItemStack getOutput() {
		return recipe.getRecipeOutput().copy();
	}

	protected static boolean matchClassName(Object obj, String name) {
		return obj.getClass().getName().compareTo(name) == 0;
	}

	protected static boolean isClassIgnored(Object obj) {
		
		for(String s: classIgnoreList)
			if(matchClassName(obj, s))
				return true;
		return false;
	}
	
	protected void scrubProjection() {
		
		if(projection == null)
			return;
		
		// Scan for the list looking for wildcards as well
		// as those items that match the input.  Sometimes
		// an author will make a cyclic recipe.
		for(int i = 0; i < projection.length; i++) {
			ItemStack stack = projection[i];
			if(stack != null)
				if(ItemHelper.itemsEqualWithMetadata(stack, inputStack))
					projection[i] = null;
				else if (stack != null && stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					stack.setItemDamage(0);
		}
			
		// Do final scrub on the list
		projection = MyUtils.compress(ItemStackHelper.coelece(projection));
	}
	
	public ItemStack[] project() {
		if (projection != null)
			return projection;

		if (recipe instanceof ShapedRecipes) {
			projection = project((ShapedRecipes) recipe);
		} else if (recipe instanceof ShapelessRecipes) {
			projection = project((ShapelessRecipes) recipe);
		} else if (recipe instanceof ShapedOreRecipe) {
			projection = project((ShapedOreRecipe) recipe);
		} else if (recipe instanceof ShapelessOreRecipe) {
			projection = project((ShapelessOreRecipe) recipe);
		} else if (matchClassName(recipe,
				"cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper")) {
			projection = projectTERecipe(recipe);
		} else if(matchClassName(recipe, "forestry.core.utils.ShapedRecipeCustom")){
			projection = projectForestryRecipe(recipe);
		} else if(!isClassIgnored(recipe)){
			ModLog.info("Unknown recipe class: %s", recipe.getClass().getName());
		}

		scrubProjection();
		
		return projection;
	}

	@Override
	public Iterator<ItemStack> iterator() {
		if (projection == null)
			projection = project();
		return new MyIterator<ItemStack>(projection);
	}

	protected static ItemStack[] project(ShapedRecipes recipe) {
		return ItemStackHelper.clone(recipe.recipeItems);
	}

	protected static ItemStack[] project(ShapelessRecipes recipe) {
		ItemStack[] tmp = new ItemStack[recipe.recipeItems.size()];
		for (int i = 0; i < tmp.length; i++)
			tmp[i] = ((ItemStack) (recipe.recipeItems.get(i))).copy();
		return tmp;
	}

	protected static ItemStack[] projectForgeRecipeList(Object[] list) {
		
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		
		for (int i = 0; i < list.length; i++) {
			Object o = list[i];

			if (o instanceof ItemStack)
				result.add(((ItemStack) o).copy());
			else if (o instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<ItemStack> t = (ArrayList<ItemStack>) o;
				result.add(ItemStackHelper.getPreferredStack(t.get(0)));
			}
		}

		return result.toArray(new ItemStack[result.size()]);
	}

	protected static ItemStack[] project(ShapedOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput());
	}

	protected static ItemStack[] project(ShapelessOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput().toArray());
	}

	protected static ItemStack[] projectTERecipe(IRecipe recipe) {

		ItemStack[] result = null;

		try {

			if (teRecipeAccessor == null) {
				Field temp = recipe.getClass().getDeclaredField("recipe");
				temp.setAccessible(true);
				teRecipeAccessor = temp;
			}

			try {
				ShapedOreRecipe shaped = (ShapedOreRecipe) teRecipeAccessor
						.get(recipe);
				result = project(shaped);
			} catch (Exception e) {
			}

		} catch (NoSuchFieldException e) {
			;
		} catch (SecurityException e) {
			;
		} catch (IllegalArgumentException e) {
			;
		}

		return result;
	}
	
	protected static ItemStack[] projectForestryRecipe(IRecipe recipe) {
		
		ItemStack[] result = null;
		
		try {

			if (forestryRecipeAccessor == null) {
				Field temp = recipe.getClass().getDeclaredField("ingredients");
				temp.setAccessible(true);
				forestryRecipeAccessor = temp;
			}

			try {
				Object[] shaped = (Object[]) forestryRecipeAccessor.get(recipe);
				result = projectForgeRecipeList(shaped);
			} catch (Exception e) {
			}

		} catch (NoSuchFieldException e) {
			;
		} catch (SecurityException e) {
			;
		} catch (IllegalArgumentException e) {
			;
		}

		return result;
	}

	public static IRecipe findRecipe(ItemStack stack) {

		for (Object o : CraftingManager.getInstance().getRecipeList()) {
			IRecipe r = (IRecipe) o;
			// craftingEquivalent
			if (ItemHelper.itemsEqualForCrafting(stack, r.getRecipeOutput()))
				return r;
		}

		return null;
	}
}

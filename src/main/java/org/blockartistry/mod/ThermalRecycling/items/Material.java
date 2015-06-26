/*
 * This file is part of ThermalRecycling, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
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

package org.blockartistry.mod.ThermalRecycling.items;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.AchievementManager;
import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;

public final class Material extends ItemBase {

	private static final Random random = XorShiftRandom.shared;
	private static final int EGG_ACCELERATION = 3000;

	public static final int PAPER_LOG = 0;
	public static final int WORMS = 1;
	public static final int LITTER_BAG = 2;
	public static final int GARDEN_SHEARS = 3;

	private static final List<ItemStack> trash = ImmutableList
			.copyOf(ItemStackHelper.getItemStacks(ModOptions
					.getInventoryTrashList()));

	public Material() {
		super("paperlog", "worms", "litterBag", "gardenShears");

		setUnlocalizedName("Material");
		setHasSubtypes(true);
		setMaxStackSize(64);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack,
			EntityPlayer player, EntityLivingBase target) {

		if (target.worldObj.isRemote || stack.getItemDamage() != WORMS)
			return false;

		if (target instanceof EntityChicken) {

			// If the chicken is a child, make it an adult. If it is
			// an adult, accelerate egg laying.
			final EntityChicken chicken = (EntityChicken) target;
			if (chicken.isChild()) {
				chicken.setGrowingAge(-1);
			} else {
				chicken.timeUntilNextEgg -= random.nextInt(EGG_ACCELERATION)
						+ EGG_ACCELERATION;
			}

			stack.stackSize--;

			return true;
		}

		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int p_77648_7_, float p_77648_8_,
			float p_77648_9_, float p_77648_10_) {

		if (!world.isRemote) {
			switch (stack.getItemDamage()) {
			case LITTER_BAG:
				if (!trash.isEmpty() && player instanceof EntityPlayerMP
						&& player.isSneaking()) {

					boolean isDirty = false;

					for (final ItemStack item : trash)
						if (ItemStackHelper.clearInventory(
								player.inventory.mainInventory, item))
							isDirty = true;

					stack.stackSize--;

					player.addStat(AchievementManager.doinTheTrash, 1);

					// Force a resync of the player inventory
					if (isDirty) {
						((EntityPlayerMP) player)
								.sendContainerToPlayer(player.inventoryContainer);
					}

					return true;
				}
				break;

			case GARDEN_SHEARS:
				if (player instanceof EntityPlayerMP) {

					if (player.isSneaking()) {

						final int xIndex = x - 1;
						final int zIndex = z - 1;

						for (int dX = 0; dX < 3; dX++)
							for (int dZ = 0; dZ < 3; dZ++)
								BlockManager.lawn.setLawn(world, xIndex + dX,
										y, zIndex + dZ, player);

					} else {
						BlockManager.lawn.setLawn(world, x, y, z, player);
					}
					
					world.playSoundAtEntity(player, "mob.sheep.shear", 0.2F, 0.0F);
					
					player.addStat(AchievementManager.shearBeauty, 1);
				}
				break;
			}
		}

		return false;
	}

	@Override
	public void register() {
		super.register();

		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(
				ItemManager.material, 1, PAPER_LOG), "ppp", "plp", "ppp", 'p',
				new ItemStack(Items.paper), 'l', new ItemStack(
						ItemManager.paperLogMaker, 1,
						OreDictionary.WILDCARD_VALUE));

		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(new ItemStack(ItemManager.material, 8,
				LITTER_BAG), "d d", "d d", "ddd", 'd', new ItemStack(
				ItemManager.debris));

		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(new ItemStack(ItemManager.material, 1,
				GARDEN_SHEARS), "i i", " i ", "s s", 'i', "ingotIron", 's',
				new ItemStack(Items.stick, 1, OreDictionary.WILDCARD_VALUE));

		GameRegistry.addRecipe(recipe);
	}
}

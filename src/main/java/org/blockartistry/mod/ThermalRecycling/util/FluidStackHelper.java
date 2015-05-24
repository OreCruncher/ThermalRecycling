/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.ThermalRecycling.util;

import cofh.lib.util.helpers.FluidHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public final class FluidStackHelper {

	public static final Fluid FLUID_WATER = FluidRegistry.getFluid("water");
	
	public static boolean applyPlayerContainerInteraction(World world, TileEntity entity, EntityPlayer player) {

        ItemStack stack = player.getCurrentEquippedItem();
        if(stack == null || !FluidContainerRegistry.isContainer(stack))
        	return false;

		boolean update = false;
		IFluidHandler handler = (IFluidHandler)entity;
		
		// Get the fluid from the item.  If there is one they are trying
		// to fill.  Otherwise they are trying to remove.
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
        if(liquid != null) {
        	update = FluidHelper.fillHandlerWithContainer(world, handler, player);
        }
        else {
        	liquid = handler.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
        	update = FluidHelper.fillContainerFromHandler(world, handler, player, liquid);
        }

        if(update)
            world.markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
		
		return update;
	}


	public static FluidStack getFluidStack(String name, int quantity) {
		return FluidRegistry.getFluidStack(name, quantity);
	}
}

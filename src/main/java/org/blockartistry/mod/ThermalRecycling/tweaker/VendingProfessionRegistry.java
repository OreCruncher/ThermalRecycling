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

package org.blockartistry.mod.ThermalRecycling.tweaker;

import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerProfessionCustom;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.VendingProfessionRegistry")
public final class VendingProfessionRegistry {

	private VendingProfessionRegistry() { }
	
	@ZenMethod
	public static int createProfession(final String name, final int fColor, final int bColor) {
		
		if(!MineTweakerUtil.checkNotNull(name, "name cannot be null"))
			return -1;
		
		if(!MineTweakerUtil.checkArgument(name.length() > 0, "invalid name length"))
			return -1;
		
		if(!MineTweakerUtil.checkArgument(DyeHelper.isValidColor(fColor), "invalid foreground color"))
			return -1;
		
		if(!MineTweakerUtil.checkArgument(DyeHelper.isValidColor(bColor), "invalid background color"))
			return -1;
		
		if(!MineTweakerUtil.checkArgument(fColor != bColor, "foreground and background colors must be different"))
			return -1;
		
		return VillagerProfessionCustom.createProfesssion(name, fColor, bColor);
	}
	
	@ZenMethod
	public static void addTrade(final int id) {
		
		if(!MineTweakerUtil.checkArgument(id >= 0, "invalid profession id"))
			return;
		
	}
}

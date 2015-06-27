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

import minetweaker.MineTweakerAPI;

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

public final class Constants {

	private Constants() {
	}
	
	// ScrapValue enum
	public static int getScrapValueNone() { return ScrapValue.NONE.ordinal(); }
	public static int getScrapValueInferior() { return ScrapValue.POOR.ordinal(); }
	public static int getScrapValueStandard() { return ScrapValue.STANDARD.ordinal(); }
	public static int getScrapValueSuperior() { return ScrapValue.SUPERIOR.ordinal(); }

	// CompostIngredient enum
	public static int getCompostValueNone() { return CompostIngredient.NONE.ordinal(); }
	public static int getCompostValueBrown() { return CompostIngredient.BROWN.ordinal(); }
	public static int getCompostValueGreen() { return CompostIngredient.GREEN.ordinal(); }
	
	private static void registerConstant(final String name, final String getter) {
		MineTweakerAPI.registerGlobalSymbol(name, MineTweakerAPI.getJavaStaticGetterSymbol
				(Constants.class, getter));
	}

	public static void register() {
		
		registerConstant("SCRAPVALUE_NONE", "getScrapValueNone");
		registerConstant("SCRAPVALUE_INFERIOR", "getScrapValueInferior");
		registerConstant("SCRAPVALUE_STANDARD", "getScrapValueStandard");
		registerConstant("SCRAPVALUE_SUPERIOR", "getScrapValueSuperior");
		
		registerConstant("COMPOSTVALUE_NONE", "getCompostValueNone");
		registerConstant("COMPOSTVALUE_BROWN", "getCompostValueBrown");
		registerConstant("COMPOSTVALUE_GREEN", "getCompostValueGreen");
	}
}

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
import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;

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
	
	// Colors
	public static int getColorBlack() { return DyeHelper.COLOR_BLACK; }
	public static int getColorRed() { return DyeHelper.COLOR_RED; }
	public static int getColorGreen() { return DyeHelper.COLOR_GREEN; }
	public static int getColorBrown() { return DyeHelper.COLOR_BROWN; }
	public static int getColorBlue() { return DyeHelper.COLOR_BLUE; }
	public static int getColorPurple() { return DyeHelper.COLOR_PURPLE; }
	public static int getColorCyan() { return DyeHelper.COLOR_CYAN; }
	public static int getColorLightGray() { return DyeHelper.COLOR_LIGHTGRAY; }
	public static int getColorGray() { return DyeHelper.COLOR_GRAY; }
	public static int getColorPink() { return DyeHelper.COLOR_PINK; }
	public static int getColorLime() { return DyeHelper.COLOR_LIME; }
	public static int getColorYellow() { return DyeHelper.COLOR_YELLOW; }
	public static int getColorLightBlue() { return DyeHelper.COLOR_LIGHTBLUE; }
	public static int getColorMagenta() { return DyeHelper.COLOR_MAGENTA; }
	public static int getColorOrange() { return DyeHelper.COLOR_ORANGE; }
	public static int getColorWhite() { return DyeHelper.COLOR_WHITE; }
	
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
		
		registerConstant("COLOR_BLACK", "getColorBlack");
		registerConstant("COLOR_RED", "getColorRed");
		registerConstant("COLOR_GREEN", "getColorGreen");
		registerConstant("COLOR_BROWN", "getColorBrown");
		registerConstant("COLOR_BLUE", "getColorBlue");
		registerConstant("COLOR_PURPLE", "getColorPurple");
		registerConstant("COLOR_CYAN", "getColorCyan");
		registerConstant("COLOR_LIGHTGRAY", "getColorLightGray");
		registerConstant("COLOR_GRAY", "getColorGray");
		registerConstant("COLOR_PINK", "getColorPink");
		registerConstant("COLOR_LIME", "getColorLime");
		registerConstant("COLOR_YELLOW", "getColorYellow");
		registerConstant("COLOR_LIGHTBLUE", "getColorLightBlue");
		registerConstant("COLOR_MAGENTA", "getColorMagenta");
		registerConstant("COLOR_ORANGE", "getColorOrange");
		registerConstant("COLOR_WHITE", "getColorWhite");
	}
}

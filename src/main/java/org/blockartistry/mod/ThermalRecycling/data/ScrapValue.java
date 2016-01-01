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

package org.blockartistry.mod.ThermalRecycling.data;

import com.google.common.base.Optional;

import net.minecraft.util.StatCollector;

public enum ScrapValue {

	// ORDERING IS IMPORTANT!
	NONE("msg.ItemScrapValue.none", 0.0F),
	POOR("msg.ItemScrapValue.poor", 1.0F),
	STANDARD("msg.ItemScrapValue.standard", 9.0F),
	SUPERIOR("msg.ItemScrapValue.superior", 81.0F);

	protected final Optional<String> translated;
	protected final float score;
	
	public Optional<String> getTranslated() {
		return translated;
	}
	
	public float getScore() {
		return this.score;
	}
	
	private ScrapValue(final String xlate, final float score) {
		if(xlate != null)
			this.translated = Optional.of(StatCollector.translateToLocal(xlate));
		else
			this.translated = Optional.absent();
		
		this.score = score;
	}
	
	public static ScrapValue map(final int value) {
		return values()[value];
	}
	
	public static ScrapValue determineValue(final float score) {
		if (score < 0.2F)
			return NONE;
		else if (score < 8.0F)
			return POOR;
		else if (score < 64.0F)
			return STANDARD;
		return SUPERIOR;
	}
}

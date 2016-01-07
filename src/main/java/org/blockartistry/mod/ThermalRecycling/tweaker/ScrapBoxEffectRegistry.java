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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.items.scrapbox.UseEffect;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.ScrapBoxEffectRegistry")
public class ScrapBoxEffectRegistry {

	private ScrapBoxEffectRegistry() {

	}

	private static ScrapValue normalizeScrapValue(final int value) {
		if (!MineTweakerUtil.checkArgument(value >= 1 && value < 4, "scrapValue must be in range 1-3"))
			return null;
		return ScrapValue.map(value);
	}

	@ZenMethod
	public static void reset(final int scrapValue) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.reset(sv);
	}

	@ZenMethod
	public static void addNoUseEffect(final int scrapValue, final int weight) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.addNoUseEffect(sv, weight);
	}

	@ZenMethod
	public static void addDropItemEffect(final int scrapValue, final int weight, final IItemStack stack,
			final int maxQuantity) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		if (!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		UseEffect.addDropItemEffect(sv, weight, item, maxQuantity);
	}

	@ZenMethod
	public static void addExperienceEffect(final int scrapValue, final int weight, final int amount) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.addExperienceEffect(sv, weight, amount);
	}

	@ZenMethod
	public static void addEnchantedBookEffect(final int scrapValue, final int weight, final int level) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.addEnchantedBookEffect(sv, weight, level);
	}

	@ZenMethod
	public static void addChestEffect(final int scrapValue, final int weight, final String chest) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		if (!MineTweakerUtil.checkArgument(chest != null && !chest.isEmpty(), "chest is invalid"))
			return;
		UseEffect.addChestEffect(sv, weight, chest);
	}

	@ZenMethod
	public static void addPotionEffect(final int scrapValue, final int weight, final int duration,
			final int amplifier) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.addPotionEffect(sv, weight, duration, amplifier);
	}

	@ZenMethod
	public static void addBonusEffect(final int scrapValue, final int weight, final int count) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		UseEffect.addBonusEffect(sv, weight, count);
	}

	@ZenMethod
	public static void addSpawnEntityEffect(final int scrapValue, final int weight, final String entityType,
			final int maxCount, final String tags) {
		final ScrapValue sv = normalizeScrapValue(scrapValue);
		if (sv == null)
			return;
		if (!MineTweakerUtil.checkArgument(entityType != null && !entityType.isEmpty(), "entityType is invalid"))
			return;
		UseEffect.addSpawnEntityEffect(sv, weight, entityType, maxCount, tags);
	}
}

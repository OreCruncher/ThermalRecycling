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

package org.blockartistry.mod.ThermalRecycling.support;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

public final class ItemDefinitions {

	public static class RubbleDrop {
		@SerializedName("item")
		public String item;
		@SerializedName("min")
		public int min;
		@SerializedName("max")
		public int max;
		@SerializedName("weight")
		public int weight;
	}

	public static class Sawdust {
		@SerializedName("count")
		public int count;
		@SerializedName("items")
		public List<String> items;
	}

	public static class RecipeAccessor {
		@SerializedName("recipe")
		public String recipe;
		@SerializedName("accessor")
		public String accessor;
	}

	public static class Handler {
		@SerializedName("handlerClass")
		public String handlerClass;
		@SerializedName("items")
		public List<String> items;
	}

	// Items to ignore recipes for
	@SerializedName("ignore")
	public List<String> ignore = ImmutableList.of();
	// Items to reveal recipes for
	@SerializedName("reveal")
	public List<String> reveal = ImmutableList.of();
	// Items that have a scrap value of NONE
	@SerializedName("none")
	public List<String> none = ImmutableList.of();
	// Items that have a scrap value of POOR
	@SerializedName("poor")
	public List<String> poor = ImmutableList.of();
	// Items that have a scrap value of STANDARD
	@SerializedName("standard")
	public List<String> standard = ImmutableList.of();
	// Items that have a scrap value of SUPERIOR
	@SerializedName("superior")
	public List<String> superior = ImmutableList.of();
	// Items that are GREEN compost ingredients
	@SerializedName("green")
	public List<String> green = ImmutableList.of();
	// Items that are BROWN compost ingredients
	@SerializedName("brown")
	public List<String> brown = ImmutableList.of();
	// Items that are scrubbed from recipe output
	@SerializedName("scrub")
	public List<String> scrub = ImmutableList.of();
	// Items that are blocked from scrapping
	@SerializedName("block")
	public List<String> block = ImmutableList.of();
	// Pile of Rubble drop registrations
	@SerializedName("rubble")
	public List<RubbleDrop> rubble = ImmutableList.of();
	// Items that can be pulverized to dirt
	@SerializedName("toDirt")
	public List<String> toDirt = ImmutableList.of();
	// Items that can be pulverized to saw dust
	@SerializedName("toSawdust")
	public List<Sawdust> toSawdust = ImmutableList.of();
	// Items that can be extracted
	@SerializedName("extract")
	public List<String> extract = ImmutableList.of();
	// List of accessors for cracking recipes
	@SerializedName("accessors")
	public List<RecipeAccessor> accessors = ImmutableList.of();
	// List of scrap handlers to register
	@SerializedName("handlers")
	public List<Handler> handlers = ImmutableList.of();

	public static ItemDefinitions load(final String modId) {
		final String fileName = modId.replaceAll("[^a-zA-Z0-9.-]", "_");
		InputStream stream = null;
		InputStreamReader reader = null;
		JsonReader reader2 = null;
		try {
			stream = ItemDefinitions.class.getResourceAsStream("/assets/recycling/data/" + fileName + ".json");
			if (stream != null) {
				reader = new InputStreamReader(stream);
				reader2 = new JsonReader(reader);
				return (ItemDefinitions) new Gson().fromJson(reader, ItemDefinitions.class);
			}
		} finally {
			try {
				if (reader2 != null)
					reader2.close();
				if (reader != null)
					reader.close();
				if (stream != null)
					stream.close();
			} catch (final Exception e) {
				;
			}
		}
		return new ItemDefinitions();
	}
}

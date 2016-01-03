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

import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public final class ItemDefinitions {
	
	public List<String> ignore = ImmutableList.of();
	public List<String> none = ImmutableList.of();
	public List<String> poor = ImmutableList.of();
	public List<String> standard = ImmutableList.of();
	public List<String> superior = ImmutableList.of();
	public List<String> green = ImmutableList.of();
	public List<String> brown = ImmutableList.of();
	public List<String> scrub = ImmutableList.of();
	public List<String> block = ImmutableList.of();
	
	@SuppressWarnings("unused")
	public static ItemDefinitions load(final String modId) {
		final String fileName = modId.replaceAll("[^a-zA-Z0-9.-]", "_");
		InputStreamReader stream = null;
		JsonReader reader = null;
		try {
			stream = new InputStreamReader(
					ItemDefinitions.class.getResourceAsStream("/assets/recycling/data/" + fileName + ".json"));
			if (stream != null) {
				reader = new JsonReader(stream);
				return (ItemDefinitions)new Gson().fromJson(reader, ItemDefinitions.class);
			}
		} finally {
			try {
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

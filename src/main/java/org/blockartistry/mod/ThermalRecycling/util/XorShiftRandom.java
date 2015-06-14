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

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class XorShiftRandom extends Random {
	
	/**
	 * General shared randomizer for when logic needs one in a
	 * pinch.
	 */
	public static final Random shared = new XorShiftRandom();

	private static final long serialVersionUID = 1422228009367463911L;
	private static Field getSeed = null;
	
	static {
		
		try {
			
			getSeed = Random.class.getDeclaredField("seed");
			getSeed.setAccessible(true);
			
		} catch(Throwable t) {
			;
		}
	}

	private long seed;

	/**
	 * Initialize a new XorShiftRandom using the current system time
	 * as a seed.
	 */
	public XorShiftRandom() {
		this(System.nanoTime());
	}
	
	/**
	 * Initialize a new XorShiftRandom using the current seed value of
	 * the provided Random.
	 * 
	 * @param random Source of the initial seed
	 */
	public XorShiftRandom(final Random random) {
		
		try {
			if(getSeed != null)
				setSeed(((AtomicLong)XorShiftRandom.getSeed.get(random)).get());
		} catch(Throwable t) {
			;
		} finally {
			if(getSeed() == 0)
				setSeed(System.nanoTime());
		}
	}
	
	/**
	 * Initialize a new XorShiftRandom using the seed value of the
	 * provided XorShiftRandom instance.
	 * 
	 * @param random
	 */
	public XorShiftRandom(final XorShiftRandom random) {
		this(random.getSeed());
	}

	/**
	 * Initialize a new XorShiftRandom using the specified seed.
	 * 
	 * @param seed
	 */
	public XorShiftRandom(final long seed) {
		this.seed = seed;
	}

	/**
	 * Gets the current seed value.
	 * 
	 * @return
	 */
	public long getSeed() {
		return seed;
	}

	/* (non-Javadoc)
	 * @see java.util.Random#setSeed(long)
	 */
	public void setSeed(final long seed) {
		this.seed = seed;
		super.setSeed(seed);
	}

	protected int next(final int nbits) {
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		x &= ((1L << nbits) - 1);
		return (int) x;
	}
}

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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

public final class ModThermalDynamics extends ModPlugin {

	private static final String[] scrapValuesPoor = new String[] { "ThermalDynamics_0:0", "ThermalDynamics_0:1" };

	private static final String[] scrapValuesStandard = new String[] {};

	private static final String[] scrapValuesSuperior = new String[] {};

	public ModThermalDynamics() {
		super(SupportedMod.THERMAL_DYNAMICS);
	}

	@Override
	public boolean initialize() {

		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Ignore covers and the impulse ducts. The ducts have a bidirectional
		// conversion recipe that can dupe hardened glass and lead.
		registerRecipesToIgnore("cover:*", "ThermalDynamics_32:2", "ThermalDynamics_32:3");
		registerScrapValues(ScrapValue.NONE, "cover:*");

		// Servos, Filters
		pulverizer.setEnergy(1200).append("ThermalDynamics:servo", "ThermalDynamics:filter").output("nuggetIron")
				.secondaryOutput("dustIron").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:servo:1", "ThermalDynamics:filter:1").output("nuggetIron")
				.secondaryOutput("dustInvar").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:servo:2", "ThermalDynamics:filter:2").output("nuggetIron")
				.secondaryOutput("dustElectrum").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:servo:3", "ThermalDynamics:filter:3").output("nuggetIron")
				.secondaryOutput("dustSignalum").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:servo:4", "ThermalDynamics:filter:4").output("nuggetIron")
				.secondaryOutput("dustEnderium").save();

		// Retrievers
		pulverizer.setEnergy(1200).append("ThermalDynamics:retriever").output("nuggetEnderium")
				.secondaryOutput("dustIron").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:retriever:1").output("nuggetEnderium")
				.secondaryOutput("dustInvar").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:retriever:2").output("nuggetEnderium")
				.secondaryOutput("dustElectrum").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:retriever:3").output("nuggetEnderium")
				.secondaryOutput("dustSignalum").save();
		pulverizer.setEnergy(1200).append("ThermalDynamics:retriever:4").output("nuggetEnderium")
				.secondaryOutput("dustEnderium").save();

		// Structural Duct
		pulverizer.append("ThermalDynamics:ThermalDynamics_48").output("nuggetLead").save();

		// Item Duct (Standard + Opaque)
		pulverizer.append("ThermalDynamics:ThermalDynamics_32").output("nuggetTin", 3).save();
		pulverizer.append("ThermalDynamics:ThermalDynamics_32:1").output("nuggetTin", 3).secondaryOutput("nuggetLead")
				.save();

		// Impulse Ducts (Standard + Opaque) - drain and turn into a base type
		fluid.append("ThermalDynamics:ThermalDynamics_32:2").output("ThermalDynamics:ThermalDynamics_32")
				.fluid("glowstone", 200).save();
		fluid.append("ThermalDynamics:ThermalDynamics_32:3").output("ThermalDynamics:ThermalDynamics_32:1")
				.fluid("glowstone", 200).save();

		// Item Duct (Warp + Opaque)
		pulverizer.append("ThermalDynamics:ThermalDynamics_32:5", "ThermalDynamics:ThermalDynamics_32:4")
				.output("nuggetTin", 3).secondaryOutput("nuggetEnderium").save();

		// Fluxuating Duct (Standard + Opaque) - drain and turn into a base type
		// for further recycling
		fluid.append("ThermalDynamics:ThermalDynamics_32:6").output("ThermalDynamics:ThermalDynamics_32")
				.fluid("redstone", 50).save();
		fluid.append("ThermalDynamics:ThermalDynamics_32:7").output("ThermalDynamics:ThermalDynamics_32:1")
				.fluid("redstone", 50).save();

		// Flux ducts - drain and smash the base type
		pulverizer.append("ThermalDynamics:ThermalDynamics_0:3").output("nuggetElectrum", 3).save();
		fluid.append("ThermalDynamics:ThermalDynamics_0:2").output("ThermalDynamics:ThermalDynamics_0:3")
				.fluid("redstone", 200).save();
		fluid.append("ThermalDynamics:ThermalDynamics_0:4").output("ThermalDynamics:ThermalDynamics_0:5")
				.fluid("redstone", 200).save();
		pulverizer.append("ThermalDynamics:ThermalDynamics_0:5").output("nuggetEnderium", 3)
				.secondaryOutput("nuggetElectrum", 3).save();

		// Fluid ducts
		pulverizer.append("ThermalDynamics:ThermalDynamics_16:2").output("nuggetInvar", 3).save();
		pulverizer.append("ThermalDynamics:ThermalDynamics_16:3").output("nuggetInvar", 3).secondaryOutput("nuggetLead")
				.save();

		pulverizer.append("ThermalDynamics:ThermalDynamics_16:4", "ThermalDynamics:ThermalDynamics_16:5")
				.output("nuggetElectrum", 3).secondaryOutput("nuggetSignalum", 3).save();

		return true;
	}
}

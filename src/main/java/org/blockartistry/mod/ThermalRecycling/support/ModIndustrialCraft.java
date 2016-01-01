package org.blockartistry.mod.ThermalRecycling.support;

import java.util.List;
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.IC2MachineRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.IC2MachineRecipeAdaptor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.IC2RecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.IC2ShapelessRecipeAccessor;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class ModIndustrialCraft extends ModPlugin {

	private static final String[] recipeIgnoreList = new String[] { "itemPlutonium", "itemUran235", "itemRubber",
			"itemScrap", };

	private static final String[] scrapValuesNone = new String[] { "itemRubber", "itemTreetap", "itemPartCarbonFibre",
			"itemPartCarbonMesh", "blockCrop", "itemArmorRubBoots", "itemRecipePart:7", "itemArmorHazmatLeggings",
			"itemArmorHazmatChestplate", "blockScaffold", "itemTurningBlanksWood:209715", "itemMugEmpty",
			"itemFuelPlantBall", "itemBoat:1", "blockRubber", "itemPartCoalBall", "itemHarz", "blockRubWood",
			"blockRubSapling", "blockRubLeaves", "blockBasalt", "itemBoat:2", "itemGrinPowder", "itemPartCarbonPlate",
			"itemRecipePart:9", "itemPartCoalBlock", "itemBoat:0", "itemBarrel" };

	private static final String[] scrapValuesPoor = new String[] { "itemCellEmpty:*", "blockMiningPipe",
			"itemArmorHazmatHelmet", "itemCable:*", "itemBatSU", "itemUran238", "itemUran235small", "blockIronScaffold",
			"itemPlutoniumSmall", "itemDynamite", "itemDynamiteSticky", "itemScrap", "itemCasing:*", "blockFenceIron",
			"itemCoin", "blockLuminatorDark", "itemTinCan", "itemFertilizer", "itemTerraWart", "itemTinCanFilled",
			"itemHops", "itemCropSeed", "itemCoffeeBeans", "itemPlates:7", "itemPartCoalChunk", "itemWeedEx", };

	private static final String[] scrapValuesStandard = new String[] { "itemCable:11", "itemDensePlates:7"

	};

	private static final String[] scrapValuesSuperior = new String[] { "itemDust2:1", "blockMachine:*",
			"blockMachine2:*", "blockMachine3:*", "blockGenerator:*", "blockElectric:*", "blockHeatGenerator:*",
			"blockKineticGenerator:*", "blockChargepad:*", "itemRecipePart:1", "itemRecipePart:2", "itemRecipePart:8",
			"itemBatLamaCrystal", "itemNightvisionGoggles", "itemArmorNanoHelmet", "itemSolarHelmet", "itemBatChargeRE",
			"itemBatChargeAdv", "itemBatChargeCrystal", "itemironrotor", "itemBoat:3", "itemScanner", "itemScannerAdv",
			"blockReactorAccessHatch", "itemPartIridium", "itemBatChargeLamaCrystal", "blockPersonal:*",
			"itemArmorQuantumChestplate", "itemIronBlockCuttingBlade", "blockReactorRedstonePort",
			"itemToolMiningLaser", "blockReactorChamber", "itemArmorAdvBatpack", "reactorPlatingHeat",
			"itemRecipePart:3", "itemToolWrenchElectric", "itemTreetapElectric", "blockNuke", "itemArmorQuantumLegs",
			"itemRecipePart:10", "itemSteamTurbineBlade", "itemSteamTurbine", "itemArmorQuantumHelmet", "windmeter",
			"blockReactorFluidPort", "itemRemote", "itemsteelrotor", "itemToolDrill", "itemToolDDrill",
			"itemArmorAlloyChestplate", "itemArmorEnergypack", "itemToolHoe", "itemDiamondBlockCuttingBlade",
			"itemToolChainsaw", "itemArmorQuantumBoots", "itemRTGPellet", "itemDoorAlloy",
			"itemAdvIronBlockCuttingBlade", "itemwcarbonrotor", "itemArmorCFPack", "itemArmorBronzeChestplate",
			"itemupgradekit:0", "obscurator", "itemRecipePart:11", "itemRecipePart:12", "upgradeModule:1",
			"upgradeModule:3", "upgradeModule:4", "upgradeModule:6", "itemCable:9", "itemOreIridium", "itemNanoSaber",
			"reactorPlatingExplosive", "itemDensePlates:*", "reactorVent", "reactorHeatSwitch",
			"reactorHeatSwitchDiamond", "itemBatCrystal", "reactorCoolantTriple", "upgradeModule:0",
			"itemToolIridiumDrill", "reactorReflectorThick", "reactorHeatSwitchCore", "reactorVentCore",
			"reactorCondensator", "reactorCondensatorLap", "reactorUraniumQuad", "reactorVentSpread",
			"reactorVentDiamond", "reactorCoolantSix", "reactorHeatSwitchSpread", "reactorMOXQuad", "reactorVentGold",
			"reactorVentSpread" };

	public ModIndustrialCraft() {
		super(SupportedMod.INDUSTRIAL_CRAFT);

		RecipeDecomposition.registerAccessor("ic2.core.AdvRecipe", new IC2RecipeAccessor());
		RecipeDecomposition.registerAccessor("ic2.core.AdvShapelessRecipe", new IC2ShapelessRecipeAccessor());
		RecipeDecomposition.registerAccessor(IC2MachineRecipeAdaptor.class, new IC2MachineRecipeAccessor());
	}

	private void processRecipes(final IMachineRecipeManager manager) {
		for (final Entry<IRecipeInput, RecipeOutput> e : manager.getRecipes().entrySet()) {
			final IC2MachineRecipeAdaptor adaptor = new IC2MachineRecipeAdaptor(e);
			final ItemStack input = RecipeDecomposition.getInput(adaptor);
			if (!ItemData.isRecipeIgnored(input)) {
				final List<ItemStack> output = RecipeDecomposition.decompose(adaptor);
				recycler.input(input).useRecipe(output).save();
			}
		}
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Fixups
		registerScrapValues(ScrapValue.POOR, "blockElectric:0");
		registerScrapValues(ScrapValue.STANDARD, "blockMachine:1", "blockMachine2:12", "blockMachine2:13",
				"blockChargepad:0", "blockElectric:3");

		processRecipes(Recipes.metalformerRolling);
		processRecipes(Recipes.metalformerExtruding);
		processRecipes(Recipes.metalformerCutting);
		processRecipes(Recipes.compressor);

		return true;
	}
}

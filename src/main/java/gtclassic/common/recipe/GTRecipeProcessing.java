package gtclassic.common.recipe;

import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import gtclassic.common.GTBlocks;
import gtclassic.common.GTItems;
import ic2.api.classic.recipe.ClassicRecipes;
import ic2.api.classic.recipe.machine.IMachineRecipeList;
import ic2.core.block.machine.low.TileEntityCompressor;
import ic2.core.block.machine.low.TileEntityExtractor;
import ic2.core.block.machine.low.TileEntityMacerator;
import ic2.core.item.recipe.entry.RecipeInputItemStack;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.util.misc.StackUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GTRecipeProcessing {

	static IMachineRecipeList smelting = ClassicRecipes.furnace;

	public static void init() {
		GameRegistry.addSmelting(GTMaterialGen.get(GTBlocks.oreSheldonite, 1), (GTMaterialGen.getIngot(GTMaterial.Platinum, 1)), 0.1F);
		maceratorUtil("oreBauxite", 1, GTMaterialGen.getDust(GTMaterial.Bauxite, 4));
		maceratorUtil("oreIridium", 1, GTMaterialGen.getDust(GTMaterial.Iridium, 2));
		TileEntityMacerator.addRecipe(GTMaterialGen.getIc2(Ic2Items.iridiumOre, 1), GTMaterialGen.getDust(GTMaterial.Iridium, 1));
		TileEntityMacerator.addRecipe("stoneMarble", 1, GTMaterialGen.getDust(GTMaterial.Calcite, 1));
		TileEntityMacerator.addRecipe("stoneLimestone", 1, GTMaterialGen.getDust(GTMaterial.Calcite, 1));
		TileEntityMacerator.addRecipe("stoneBasalt", 1, GTMaterialGen.getDust(GTMaterial.Basalt, 1));
		TileEntityMacerator.addRecipe(GTMaterialGen.get(Items.FLINT, 1), GTMaterialGen.getDust(GTMaterial.Flint, 1));
		TileEntityMacerator.addRecipe("enderpearl", 1, GTMaterialGen.getDust(GTMaterial.EnderPearl, 1));
		TileEntityMacerator.addRecipe(GTMaterialGen.get(Items.ENDER_EYE, 1), GTMaterialGen.getDust(GTMaterial.EnderEye, 2));
		TileEntityMacerator.addRecipe("gemEmerald", 1, GTMaterialGen.getDust(GTMaterial.Emerald, 1));
		TileEntityExtractor.addRecipe("oreRuby", 1, GTMaterialGen.getGem(GTMaterial.Ruby, 3), 0.1F);
		TileEntityExtractor.addRecipe("oreSapphire", 1, GTMaterialGen.getGem(GTMaterial.Sapphire, 3), 0.1F);
		TileEntityCompressor.addRecipe("dustEmerald", 1, GTMaterialGen.get(Items.EMERALD, 1));
		TileEntityCompressor.addRecipe("dustCarbon", 8, GTMaterialGen.getIc2(Ic2Items.carbonFiber, 1));
		TileEntityCompressor.addRecipe("dustUranium", 1, GTMaterialGen.getIc2(Ic2Items.uraniumIngot, 1), 0.3F);
		TileEntityCompressor.addRecipe("dustThorium", 1, GTMaterialGen.getIngot(GTMaterial.Thorium, 1));
		ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GTMaterial.Sodium), 3800, 8);
		ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GTMaterial.Hydrogen), 950, 16);
		ClassicRecipes.fluidGenerator.addEntry(GTMaterialGen.getFluid(GTMaterial.Methane), 3000, 16);
		ItemStack fullCan = GTMaterialGen.get(GTItems.sprayCan);
		NBTTagCompound nbt = StackUtil.getNbtData(fullCan);
		nbt.setInteger("color", 15);
		ClassicRecipes.canningMachine.registerCannerItem(GTMaterialGen.get(GTItems.sprayCanEmpty), new RecipeInputItemStack(GTMaterialGen.getTube(GTMaterial.MagicDye, 1)), fullCan);
	}

	/*
	 * Adds a macerator recipe while removing duplicates generated by ic2c
	 */
	public static void maceratorUtil(String input, int amount, ItemStack output) {
		TileEntityMacerator.oreBlacklist.add(input);
		TileEntityMacerator.addRecipe(input, amount, output);
	}
}
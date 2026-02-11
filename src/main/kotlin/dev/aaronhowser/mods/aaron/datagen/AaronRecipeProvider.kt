package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.concurrent.CompletableFuture

abstract class AaronRecipeProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries) {

	protected fun shapelessRecipe(
		output: ItemLike,
		count: Int,
		requirements: List<Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapelessRecipeBuilder {
		var temp = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, count)

		for (requirement in requirements) {
			temp = temp.requires(requirement)
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun shapelessRecipe(
		output: ItemLike,
		requirements: List<Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	) = shapelessRecipe(output, 1, requirements, unlockedByName, unlockedByCriterion)

	protected fun shapedRecipe(
		output: ItemStack,
		patterns: String,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)

		for (pattern in patterns.split(",")) {
			temp = temp.pattern(pattern)
		}

		for ((key, ingredient) in definitions) {
			temp = temp.define(key, ingredient)
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun shapedRecipe(
		output: ItemLike,
		count: Int,
		patterns: String,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, count)

		for (pattern in patterns.split(",")) {
			temp = temp.pattern(pattern)
		}

		for ((key, ingredient) in definitions) {
			temp = temp.define(key, ingredient)
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun shapedRecipe(
		output: ItemLike,
		patterns: String,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		return shapedRecipe(output, 1, patterns, definitions, unlockedByName, unlockedByCriterion)
	}

}
package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

abstract class AaronRecipeProvider(
	output: PackOutput
) : RecipeProvider(output) {

	protected fun shapelessRecipe(
		output: ItemLike,
		count: Int,
		requirements: List<Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: CriterionTriggerInstance = has(ItemTags.LOGS)
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
		unlockedByCriterion: CriterionTriggerInstance = has(ItemTags.LOGS)
	): ShapelessRecipeBuilder {
		return shapelessRecipe(output, 1, requirements, unlockedByName, unlockedByCriterion)
	}

	protected fun shapedRecipe(
		output: ItemStack,
		patterns: String,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: CriterionTriggerInstance = has(ItemTags.LOGS)
	): RecipeWithItemStackOutputBuilder {
		var temp = RecipeWithItemStackOutputBuilder(RecipeCategory.MISC, output)

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
		unlockedByCriterion: CriterionTriggerInstance = has(ItemTags.LOGS)
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
		unlockedByCriterion: CriterionTriggerInstance = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		return shapedRecipe(output, 1, patterns, definitions, unlockedByName, unlockedByCriterion)
	}

}
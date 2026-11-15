package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

abstract class AaronRecipeProvider(
	registries: HolderLookup.Provider,
	output: RecipeOutput
) : RecipeProvider(registries, output) {

	protected fun TagKey<Item>.asIngredient(): Ingredient = tag(this)

	protected fun recipeKey(identifier: Identifier): ResourceKey<Recipe<*>> {
		return ResourceKey.create(Registries.RECIPE, identifier)
	}

	protected fun RecipeBuilder.save(identifier: Identifier) {
		save(output, recipeKey(identifier))
	}

	protected fun shapelessRecipe(
		output: ItemLike,
		count: Int,
		requirements: List<Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapelessRecipeBuilder {
		var temp = shapeless(RecipeCategory.MISC, output, count)

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
		patterns: List<String>,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = shaped(RecipeCategory.MISC, net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(output))

		for (pattern in patterns) {
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
		patterns: List<String>,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = shaped(RecipeCategory.MISC, output, count)

		for (pattern in patterns) {
			temp = temp.pattern(pattern)
		}

		for ((key, ingredient) in definitions) {
			temp = temp.define(key, ingredient)
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun shapedRecipe(
		output: ItemLike,
		patterns: List<String>,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		return shapedRecipe(output, 1, patterns, definitions, unlockedByName, unlockedByCriterion)
	}

}
package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.*
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

abstract class AaronRecipeProvider(
	registries: HolderLookup.Provider,
	output: RecipeOutput
) : RecipeProvider(registries, output) {

	fun ItemLike.asIngredient(): Ingredient = Ingredient.of(this)
	fun TagKey<Item>.asIngredient(): Ingredient = Ingredient.of(items.getOrThrow(this))
	fun ItemStackTemplate.asIngredient(strict: Boolean = false): Ingredient {
		return if (components.isEmpty) {
			Ingredient.of(this.item.value())
		} else {
			DataComponentIngredient.of(strict, this)
		}
	}

//	fun ItemLike.asIngredient(
//		predicate: DataComponentPredicate,
//		strict: Boolean = false
//	): Ingredient {
//		return DataComponentIngredient.of(strict, predicate, this)
//	}
//
//	fun <T> ItemLike.asIngredient(
//		componentType: DataComponentType<in T>,
//		component: T,
//	): Ingredient {
//		val predicate = DataComponentPredicate.Single(componentType, component)
//		return asIngredient(predicate)
//	}

	protected fun shapeless(
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

	protected fun shapeless(
		output: ItemLike,
		requirements: List<Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapelessRecipeBuilder {
		return shapeless(output, 1, requirements, unlockedByName, unlockedByCriterion)
	}

	protected fun shapedRecipe(
		output: ItemStackTemplate,
		patterns: String,
		definitions: Map<Char, Ingredient>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = shaped(RecipeCategory.MISC, output)

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
		var temp = shaped(RecipeCategory.MISC, output, count)

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
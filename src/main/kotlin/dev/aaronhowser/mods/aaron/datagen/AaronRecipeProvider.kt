package dev.aaronhowser.mods.aaron.datagen

import dev.aaronhowser.mods.aaron.AaronExtensions.asIngredient
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

abstract class AaronRecipeProvider(
	output: PackOutput
) : RecipeProvider(output) {

	protected sealed class IngredientType {
		data class TagKeyIng(val tagKey: TagKey<Item>) : IngredientType()
		data class ItemLikeIng(val item: ItemLike) : IngredientType()
		data class ItemStackIng(val itemStack: ItemStack) : IngredientType()

		fun getIngredient(): Ingredient {
			return when (this) {
				is TagKeyIng -> tagKey.asIngredient()
				is ItemLikeIng -> item.asIngredient()
				is ItemStackIng -> itemStack.asIngredient()
			}
		}
	}

	protected fun ing(tagKey: TagKey<Item>) = IngredientType.TagKeyIng(tagKey)
	protected fun ing(item: ItemLike) = IngredientType.ItemLikeIng(item)
	protected fun ing(itemStack: ItemStack) = IngredientType.ItemStackIng(itemStack)

	protected fun <T : IngredientType> shapedRecipe(
		output: ItemStack,
		patterns: String,
		definitions: Map<Char, T>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: AbstractCriterionTriggerInstance = has(ItemTags.LOGS)
	): RecipeWithItemStackOutputBuilder {
		var temp = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output)

		for (pattern in patterns.split(",")) {
			temp = temp.pattern(pattern)
		}

		for (definition in definitions) {
			temp = when (val ing = definition.value) {
				is IngredientType.TagKeyIng -> temp.define(definition.key, ing.getIngredient())
				is IngredientType.ItemLikeIng -> temp.define(definition.key, ing.getIngredient())
				is IngredientType.ItemStackIng -> temp.define(definition.key, ing.getIngredient())
			}
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun <T : IngredientType> shapedRecipe(
		output: ItemLike,
		count: Int,
		patterns: String,
		definitions: Map<Char, T>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		var temp = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, count)

		for (pattern in patterns.split(",")) {
			temp = temp.pattern(pattern)
		}

		for (definition in definitions) {
			temp = when (val ing = definition.value) {
				is IngredientType.TagKeyIng -> temp.define(definition.key, ing.getIngredient())
				is IngredientType.ItemLikeIng -> temp.define(definition.key, ing.getIngredient())
				is IngredientType.ItemStackIng -> temp.define(definition.key, ing.getIngredient())
			}
		}

		return temp.unlockedBy(unlockedByName, unlockedByCriterion)
	}

	protected fun <T : IngredientType> shapedRecipe(
		output: ItemLike,
		patterns: String,
		definitions: Map<Char, T>,
		unlockedByName: String = "has_log",
		unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
	): ShapedRecipeBuilder {
		return shapedRecipe(output, 1, patterns, definitions, unlockedByName, unlockedByCriterion)
	}

}
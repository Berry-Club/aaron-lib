package dev.aaronhowser.mods.aaron.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.CraftingRecipeBuilder
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

open class RecipeWithItemStackOutputBuilder(
	private val category: RecipeCategory = RecipeCategory.MISC,
	private val result: ItemStack
) : CraftingRecipeBuilder(), RecipeBuilder {

	private val rows: MutableList<String> = mutableListOf()
	private val definitions: MutableMap<Char, Ingredient> = mutableMapOf()
	private val advancement: Advancement.Builder = Advancement.Builder.recipeAdvancement()

	private var group: String? = null
	private var showNotification: Boolean = true

	fun define(symbol: Char, ingredient: Ingredient): RecipeWithItemStackOutputBuilder {
		require(!symbol.isWhitespace()) { "Symbol '$symbol' is whitespace!" }
		require(!definitions.containsKey(symbol)) { "Symbol '$symbol' is already defined!" }

		definitions[symbol] = ingredient
		return this
	}

	fun pattern(row: String): RecipeWithItemStackOutputBuilder {
		if (rows.isNotEmpty()) {
			require(row.length == rows[0].length) { "Row '$row' is not the correct width!" }
		}

		rows.add(row)
		return this
	}

	fun showNotification(show: Boolean): RecipeWithItemStackOutputBuilder {
		showNotification = show
		return this
	}

	override fun unlockedBy(pCriterionName: String, pCriterionTrigger: CriterionTriggerInstance): RecipeWithItemStackOutputBuilder {
		advancement.addCriterion(pCriterionName, pCriterionTrigger)
		return this
	}

	override fun group(pGroupName: String?): RecipeWithItemStackOutputBuilder {
		group = pGroupName
		return this
	}

	override fun getResult(): Item = error("Purposefully unimplemented")
	fun getResultStack(): ItemStack = result

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe>, pRecipeId: ResourceLocation) {
		ensureValid(pRecipeId)
		advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
			.rewards(AdvancementRewards.Builder.recipe(pRecipeId))
			.requirements(RequirementsStrategy.OR)

		pFinishedRecipeConsumer.accept(
			Result(
				determineBookCategory(category),
				pRecipeId,
				result,
				group ?: "",
				rows,
				definitions,
				advancement,
				pRecipeId.withPrefix("recipes/" + category.folderName + "/"),
				showNotification
			)
		)
	}

	private fun ensureValid(pRecipeId: ResourceLocation) {
		require(rows.isNotEmpty()) { "No pattern is defined for shaped recipe $pRecipeId!" }
		require(definitions.isNotEmpty()) { "No symbols are defined for shaped recipe $pRecipeId!" }

		val patternSymbols = rows.flatMap { it.toList() }.toSet().filter { !it.isWhitespace() }
		val definedSymbols = definitions.keys

		for (symbol in patternSymbols) {
			require(definedSymbols.contains(symbol)) { "Symbol '$symbol' used in pattern but not defined for shaped recipe $pRecipeId!" }
		}

		for (symbol in definedSymbols) {
			require(patternSymbols.contains(symbol)) { "Symbol '$symbol' defined but not used in pattern for shaped recipe $pRecipeId!" }
		}

		require(result.count > 0) { "Result count must be at least 1 for shaped recipe $pRecipeId!" }
		require(rows.size != 1 && rows[0].length != 1) { "Shaped recipe $pRecipeId is 1x1, use a shapeless recipe instead!" }
		require(advancement.criteria.isNotEmpty()) { "No way to unlock shaped recipe $pRecipeId!" }
	}

	protected class Result(
		category: CraftingBookCategory,
		private val id: ResourceLocation,
		private val result: ItemStack,
		private val group: String,
		private val pattern: List<String>,
		private val definitions: Map<Char, Ingredient>,
		private val advancement: Advancement.Builder,
		private val advancementId: ResourceLocation,
		private val showNotification: Boolean
	) : CraftingResult(category) {

		override fun getId(): ResourceLocation = id
		override fun serializeAdvancement(): JsonObject = advancement.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId

		override fun getType(): RecipeSerializer<*> = RecipeSerializer.SHAPED_RECIPE

		override fun serializeRecipeData(pJson: JsonObject) {
			super.serializeRecipeData(pJson)

			if (group.isNotEmpty()) {
				pJson.addProperty("group", group)
			}

			val pattern = JsonArray()
			for (row in this.pattern) {
				pattern.add(row)
			}
			pJson.add("pattern", pattern)

			val definitions = JsonObject()
			for ((symbol, ingredient) in this.definitions) {
				definitions.add(symbol.toString(), ingredient.toJson())
			}
			pJson.add("key", definitions)

			val result = JsonObject()
			result.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result.item).toString())
			if (this.result.count > 1) {
				result.addProperty("count", this.result.count)
			}
			if (this.result.hasTag()) {
				result.addProperty("nbt", this.result.tag.toString())
			}
			pJson.add("result", result)

			pJson.addProperty("show_notification", showNotification)
		}
	}

}
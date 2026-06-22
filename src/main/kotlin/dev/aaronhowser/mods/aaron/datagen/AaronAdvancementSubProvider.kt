package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import java.util.concurrent.CompletableFuture

abstract class AaronAdvancementSubProvider(
	val modId: String,
	val lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AdvancementProvider.AdvancementGenerator {

	protected fun advancement(): Advancement.Builder = Advancement.Builder.advancement()

	protected fun hasItems(vararg items: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun hasItems(vararg items: ItemPredicate): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun hasItems(vararg items: ItemPredicate.Builder): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun Advancement.Builder.has(item: ItemLike): Advancement.Builder {
		val name = item.asItem().builtInRegistryHolder().registeredName
		return addCriterion("has_$name", hasItems(item))
	}

	protected fun Advancement.Builder.has(name: String, item: ItemLike): Advancement.Builder =
		addCriterion(name, hasItems(item))

	protected fun Advancement.Builder.hasAll(vararg items: ItemLike): Advancement.Builder {
		for (item in items) {
			has(item)
		}

		return requirements(AdvancementRequirements.Strategy.AND)
	}

	protected fun Advancement.Builder.hasAny(vararg items: ItemLike): Advancement.Builder {
		for (item in items) {
			has(item)
		}

		return requirements(AdvancementRequirements.Strategy.OR)
	}

	protected fun Advancement.Builder.addCriteria(vararg criteria: Pair<String, Criterion<*>>): Advancement.Builder {
		for ((name, criterion) in criteria) {
			addCriterion(name, criterion)
		}

		return this
	}

	protected fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
		return addCriterion(
			"impossible",
			CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
		)
	}

	protected fun Advancement.Builder.allRequirements(): Advancement.Builder =
		requirements(AdvancementRequirements.Strategy.AND)

	protected fun Advancement.Builder.anyRequirements(): Advancement.Builder =
		requirements(AdvancementRequirements.Strategy.OR)

	protected fun Advancement.Builder.requirementsAllOf(vararg names: String): Advancement.Builder =
		requirements(AdvancementRequirements.allOf(names.asList()))

	protected fun Advancement.Builder.requirementsAnyOf(vararg names: String): Advancement.Builder =
		requirements(AdvancementRequirements.anyOf(names.asList()))

	protected fun Advancement.Builder.display(
		icon: ItemLike,
		title: Component,
		description: Component,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return display(
			icon,
			title,
			description,
			null,
			type,
			showToast,
			announceToChat,
			hidden
		)
	}

	protected fun Advancement.Builder.display(
		icon: ItemStack,
		title: Component,
		description: Component,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return display(
			icon,
			title,
			description,
			null,
			type,
			showToast,
			announceToChat,
			hidden
		)
	}

	protected fun Advancement.Builder.displayWithBackground(
		icon: ItemLike,
		title: Component,
		description: Component,
		background: ResourceLocation,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return display(
			icon,
			title,
			description,
			background,
			type,
			showToast,
			announceToChat,
			hidden
		)
	}

	protected fun Advancement.Builder.displayWithBackground(
		icon: ItemStack,
		title: Component,
		description: Component,
		background: ResourceLocation,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return display(
			icon,
			title,
			description,
			background,
			type,
			showToast,
			announceToChat,
			hidden
		)
	}

	protected fun modLoc(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

}
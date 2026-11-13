package dev.aaronhowser.mods.aaron.datagen

import dev.aaronhowser.mods.aaron.advancement.PlayerActionTrigger
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.ImpossibleTrigger
import net.minecraft.advancements.triggers.InventoryChangeTrigger
import net.minecraft.data.advancements.AdvancementSubProvider
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

abstract class AaronAdvancementSubProvider(
	val modId: String,
	output: BootstrapContext<Advancement>
) : AdvancementSubProvider(output) {

	protected fun advancement(): Advancement.Builder = Advancement.Builder.advancement()

	protected fun hasItems(vararg items: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun hasItems(vararg items: ItemPredicate): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun hasItems(vararg items: ItemPredicate.Builder): Criterion<InventoryChangeTrigger.TriggerInstance> =
		InventoryChangeTrigger.TriggerInstance.hasItems(*items)

	protected fun playerAction(action: Identifier): Criterion<PlayerActionTrigger.TriggerInstance> =
		PlayerActionTrigger.TriggerInstance.action(action)

	protected fun Advancement.Builder.has(item: ItemLike): Advancement.Builder {
		val name = item.asItem().builtInRegistryHolder().key().identifier().path
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
			icon.asItem(),
			title,
			description,
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
			ItemStackTemplate.fromNonEmptyStack(icon),
			title,
			description,
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
		background: Identifier,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return rootDisplay(
			icon.asItem(),
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
		background: Identifier,
		type: AdvancementType = AdvancementType.TASK,
		showToast: Boolean = true,
		announceToChat: Boolean = true,
		hidden: Boolean = false
	): Advancement.Builder {
		return rootDisplay(
			ItemStackTemplate.fromNonEmptyStack(icon),
			title,
			description,
			background,
			type,
			showToast,
			announceToChat,
			hidden
		)
	}

	protected fun modLoc(path: String): Identifier = Identifier.fromNamespaceAndPath(modId, path)

}
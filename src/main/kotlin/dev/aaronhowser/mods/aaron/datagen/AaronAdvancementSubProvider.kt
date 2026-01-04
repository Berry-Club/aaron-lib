package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import java.util.concurrent.CompletableFuture

abstract class AaronAdvancementSubProvider(
	val lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AdvancementProvider.AdvancementGenerator {

	protected fun advancement(): Advancement.Builder = Advancement.Builder.advancement()

	protected fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
		return addCriterion(
			"impossible",
			CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
		)
	}

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

}
package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.FrameType
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ItemLike
import net.minecraftforge.common.data.ForgeAdvancementProvider
import java.util.concurrent.CompletableFuture

abstract class AaronAdvancementSubProvider(
	val lookupProvider: CompletableFuture<HolderLookup.Provider>
) : ForgeAdvancementProvider.AdvancementGenerator {

	protected fun advancement(): Advancement.Builder = Advancement.Builder.advancement()

	// FIXME
//	protected fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
//		return addCriterion(
//			"impossible",
//			CriteriaTriggers.IMPOSSIBLE.createInstance(ImpossibleTrigger.TriggerInstance())
//		)
//	}

	protected fun Advancement.Builder.display(
		icon: ItemLike,
		title: Component,
		description: Component,
		type: FrameType = FrameType.TASK,
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
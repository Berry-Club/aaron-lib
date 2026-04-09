package dev.aaronhowser.mods.aaron.datagen

//abstract class AaronAdvancementSubProvider(
//	val lookupProvider: CompletableFuture<HolderLookup.Provider>
//) : AdvancementProvider.AdvancementGenerator {
//
//	companion object {
//		protected fun advancement(): Advancement.Builder = Advancement.Builder.advancement()
//
//		fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
//			return addCriterion(
//				"impossible",
//				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
//			)
//		}
//
//		fun Advancement.Builder.display(
//			icon: ItemLike,
//			title: Component,
//			description: Component,
//			type: AdvancementType = AdvancementType.TASK,
//			showToast: Boolean = true,
//			announceToChat: Boolean = true,
//			hidden: Boolean = false
//		): Advancement.Builder {
//			return display(
//				icon,
//				title,
//				description,
//				null,
//				type,
//				showToast,
//				announceToChat,
//				hidden
//			)
//		}
//	}
//
//}
package dev.aaronhowser.mods.aaron.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.registry.actual.AaronCriterionTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import java.util.Optional

class PlayerActionTrigger : SimpleCriterionTrigger<PlayerActionTrigger.TriggerInstance>() {

	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, action: Identifier) {
		trigger(player) { instance -> instance.action == action }
	}

	data class TriggerInstance(
		private val playerPredicate: Optional<ContextAwarePredicate>,
		val action: Identifier
	) : SimpleInstance {

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
				instance.group(
					EntityPredicate.ADVANCEMENT_CODEC
						.optionalFieldOf("player")
						.forGetter(TriggerInstance::playerPredicate),
					Identifier.CODEC
						.fieldOf("action")
						.forGetter(TriggerInstance::action)
				).apply(instance, ::TriggerInstance)
			}

			fun action(action: Identifier): Criterion<TriggerInstance> =
				create(Optional.empty(), action)

			fun action(
				player: EntityPredicate.Builder,
				action: Identifier
			): Criterion<TriggerInstance> {
				return create(
					Optional.of(EntityPredicate.wrap(player.build())),
					action
				)
			}

			private fun create(
				player: Optional<ContextAwarePredicate>,
				action: Identifier
			): Criterion<TriggerInstance> {
				return AaronCriterionTriggers.PLAYER_ACTION.get()
					.createCriterion(TriggerInstance(player, action))
			}
		}
	}

}
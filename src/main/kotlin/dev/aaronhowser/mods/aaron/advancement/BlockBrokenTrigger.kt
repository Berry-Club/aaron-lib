package dev.aaronhowser.mods.aaron.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.registry.actual.AaronCriterionTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderSet
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import java.util.Optional

class BlockBrokenTrigger : SimpleCriterionTrigger<BlockBrokenTrigger.TriggerInstance>() {

	override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

	fun trigger(player: ServerPlayer, level: ServerLevel, position: BlockPos) {
		trigger(player) { instance -> instance.matches(level, position) }
	}

	data class TriggerInstance(
		private val playerPredicate: Optional<ContextAwarePredicate>,
		val block: Optional<BlockPredicate>
	) : SimpleInstance {

		override fun player(): Optional<ContextAwarePredicate> = playerPredicate

		fun matches(level: ServerLevel, position: BlockPos): Boolean {
			return block.isEmpty || block.get().matches(level, position)
		}

		companion object {
			val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
				instance.group(
					EntityPredicate.ADVANCEMENT_CODEC
						.optionalFieldOf("player")
						.forGetter(TriggerInstance::playerPredicate),
					BlockPredicate.CODEC
						.optionalFieldOf("block")
						.forGetter(TriggerInstance::block)
				).apply(instance, ::TriggerInstance)
			}

			fun anyBlock(): Criterion<TriggerInstance> = create(Optional.empty())

			fun block(block: Block): Criterion<TriggerInstance> {
				val predicate = BlockPredicate(
					Optional.of(HolderSet.direct(block.builtInRegistryHolder())),
					Optional.empty(),
					Optional.empty()
				)

				return block(predicate)
			}

			fun block(state: BlockState): Criterion<TriggerInstance> {
				val properties = StatePropertiesPredicate.Builder.properties()

				for ((property, value) in state.values) {
					addProperty(properties, property, value)
				}

				val predicate = BlockPredicate(
					Optional.of(HolderSet.direct(state.block.builtInRegistryHolder())),
					properties.build(),
					Optional.empty()
				)

				return block(predicate)
			}

			fun block(predicate: BlockPredicate.Builder): Criterion<TriggerInstance> =
				block(predicate.build())

			fun block(predicate: BlockPredicate): Criterion<TriggerInstance> =
				create(Optional.of(predicate))

			fun block(
				player: EntityPredicate.Builder,
				predicate: BlockPredicate.Builder
			): Criterion<TriggerInstance> {
				return create(
					Optional.of(predicate.build()),
					Optional.of(EntityPredicate.wrap(player.build()))
				)
			}

			private fun create(
				block: Optional<BlockPredicate>,
				player: Optional<ContextAwarePredicate> = Optional.empty()
			): Criterion<TriggerInstance> {
				return AaronCriterionTriggers.BLOCK_BROKEN.get()
					.createCriterion(TriggerInstance(player, block))
			}

			@Suppress("UNCHECKED_CAST")
			private fun addProperty(
				builder: StatePropertiesPredicate.Builder,
				property: Property<*>,
				value: Comparable<*>
			) {
				val typedProperty = property as Property<Comparable<Any>>
				builder.hasProperty(typedProperty, typedProperty.getName(value as Comparable<Any>))
			}
		}
	}

}
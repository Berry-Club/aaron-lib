package dev.aaronhowser.mods.aaron.misc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.*
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

data class ImprovedEntityPredicate(
	val entityType: Optional<EntityTypePredicate> = Optional.empty(),
	val movement: Optional<MovementPredicate> = Optional.empty(),
	val effects: Optional<MobEffectsPredicate> = Optional.empty(),
	val nbt: Optional<NbtPredicate> = Optional.empty(),
	val flags: Optional<EntityFlagsPredicate> = Optional.empty(),
	val periodicTick: Optional<Int> = Optional.empty(),
	val vehicle: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val passenger: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val targetedEntity: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val team: Optional<String> = Optional.empty(),
	val slots: Optional<SlotsPredicate> = Optional.empty()
) {


	@OptIn(ExperimentalContracts::class)
	fun matches(entity: Entity?): Boolean {
		contract { returns(true) implies (entity != null) }
		if (entity == null) return false

		if (entityType.isPresent && !entityType.get().matches(entity.type)) return false
		if (movement.isPresent) {
			val motion = entity.knownMovement.scale(20.0) // Why scale?
			if (!movement.get().matches(motion.x, motion.y, motion.z, entity.fallDistance.toDouble())) return false
		}

		if (effects.isPresent && !effects.get().matches(entity)) return false
		if (nbt.isPresent && !nbt.get().matches(entity)) return false
		if (flags.isPresent && !flags.get().matches(entity)) return false

		if (periodicTick.isPresent && entity.tickCount % periodicTick.get() != 0) return false

		if (vehicle.isPresent && !vehicle.get().matches(entity.vehicle)) return false
		if (passenger.isPresent && entity.passengers.none { passenger.get().matches(it) }) return false
		if (targetedEntity.isPresent && !targetedEntity.get().matches((entity as? Mob)?.target)) return false

		if (team.isPresent && !team.get().equals(entity.team?.name, ignoreCase = false)) return false
		if (slots.isPresent && !slots.get().matches(entity)) return false

		return true
	}

	companion object {
		val CODEC: Codec<ImprovedEntityPredicate> =
			Codec.recursive("ImprovedEntityPredicate") { codec ->
				RecordCodecBuilder.create { instance ->
					instance.group(
						EntityTypePredicate.CODEC
							.optionalFieldOf("entity_type")
							.forGetter(ImprovedEntityPredicate::entityType),
						MovementPredicate.CODEC
							.optionalFieldOf("movement")
							.forGetter(ImprovedEntityPredicate::movement),
						MobEffectsPredicate.CODEC
							.optionalFieldOf("effects")
							.forGetter(ImprovedEntityPredicate::effects),
						NbtPredicate.CODEC
							.optionalFieldOf("nbt")
							.forGetter(ImprovedEntityPredicate::nbt),
						EntityFlagsPredicate.CODEC
							.optionalFieldOf("flags")
							.forGetter(ImprovedEntityPredicate::flags),
						Codec.INT
							.optionalFieldOf("periodic_tick")
							.forGetter(ImprovedEntityPredicate::periodicTick),
						codec
							.optionalFieldOf("vehicle")
							.forGetter(ImprovedEntityPredicate::vehicle),
						codec
							.optionalFieldOf("passenger")
							.forGetter(ImprovedEntityPredicate::passenger),
						codec
							.optionalFieldOf("targeted_entity")
							.forGetter(ImprovedEntityPredicate::targetedEntity),
						Codec.STRING
							.optionalFieldOf("team")
							.forGetter(ImprovedEntityPredicate::team),
						SlotsPredicate.CODEC
							.optionalFieldOf("slots")
							.forGetter(ImprovedEntityPredicate::slots)
					).apply(instance, ::ImprovedEntityPredicate)
				}
			}

		// FIXME
		val STREAM_CODEC: StreamCodec<ByteBuf, ImprovedEntityPredicate> =
			ByteBufCodecs.fromCodec(CODEC)
	}
}
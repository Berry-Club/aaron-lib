package dev.aaronhowser.mods.aaron.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.critereon.*
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import java.util.*

sealed interface ImprovedEntityPredicate {

	fun matches(entity: Entity?): Boolean
	fun getType(): Type

	companion object {
		val CODEC: Codec<ImprovedEntityPredicate> = Type.CODEC.dispatch(
			ImprovedEntityPredicate::getType,
			Type::codec
		)
	}

	enum class Type(
		val id: String,
		val codec: MapCodec<out ImprovedEntityPredicate>
	) : StringRepresentable {
		ALL("all", All.CODEC),
		DETAILED("detailed", Detailed.CODEC)
		;

		override fun getSerializedName(): String = this.id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Type> = StringRepresentable.fromEnum(Type::values)
		}
	}

	class All : ImprovedEntityPredicate {
		override fun matches(entity: Entity?) = true
		override fun getType(): Type = Type.ALL

		private constructor()

		companion object {
			val INSTANCE = All()

			val CODEC: MapCodec<All> = MapCodec.unit { INSTANCE }
		}
	}

	class Detailed(
		val entityType: Optional<EntityTypePredicate> = Optional.empty(),
		val movement: Optional<MovementPredicate> = Optional.empty(),
		val effects: Optional<MobEffectsPredicate> = Optional.empty(),
		val nbt: Optional<NbtPredicate> = Optional.empty(),
		val flags: Optional<EntityFlagsPredicate> = Optional.empty(),
		val periodicTick: Optional<Int> = Optional.empty(),
		val team: Optional<String> = Optional.empty(),
		val slots: Optional<SlotsPredicate> = Optional.empty()
	) : ImprovedEntityPredicate {

		override fun matches(entity: Entity?): Boolean {
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

			if (team.isPresent && !team.get().equals(entity.team?.name, ignoreCase = false)) return false
			if (slots.isPresent && !slots.get().matches(entity)) return false

			return true
		}

		override fun getType(): Type = Type.DETAILED

		companion object {
			val CODEC: MapCodec<Detailed> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						EntityTypePredicate.CODEC
							.optionalFieldOf("entity_type")
							.forGetter(Detailed::entityType),
						MovementPredicate.CODEC
							.optionalFieldOf("movement")
							.forGetter(Detailed::movement),
						MobEffectsPredicate.CODEC
							.optionalFieldOf("effects")
							.forGetter(Detailed::effects),
						NbtPredicate.CODEC
							.optionalFieldOf("nbt")
							.forGetter(Detailed::nbt),
						EntityFlagsPredicate.CODEC
							.optionalFieldOf("flags")
							.forGetter(Detailed::flags),
						Codec.INT
							.optionalFieldOf("periodic_tick")
							.forGetter(Detailed::periodicTick),
						Codec.STRING
							.optionalFieldOf("team")
							.forGetter(Detailed::team),
						SlotsPredicate.CODEC
							.optionalFieldOf("slots")
							.forGetter(Detailed::slots)
					).apply(instance, ::Detailed)
				}
		}
	}

}
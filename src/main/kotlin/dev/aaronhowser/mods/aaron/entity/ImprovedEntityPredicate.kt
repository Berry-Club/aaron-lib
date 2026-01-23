package dev.aaronhowser.mods.aaron.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.*
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import java.util.*

sealed interface ImprovedEntityPredicate {

	fun matches(entity: Entity?): Boolean
	fun getType(): Type

	companion object {
		val CODEC: Codec<ImprovedEntityPredicate> =
			Type.CODEC.dispatch(
				ImprovedEntityPredicate::getType,
				Type::codec
			)

		val STREAM_CODEC: StreamCodec<ByteBuf, ImprovedEntityPredicate> =
			Type.STREAM_CODEC.dispatch(
				ImprovedEntityPredicate::getType,
				Type::streamCodec
			)
	}

	enum class Type(
		val id: String,
		val codec: MapCodec<out ImprovedEntityPredicate>,
		val streamCodec: StreamCodec<ByteBuf, out ImprovedEntityPredicate>
	) : StringRepresentable {
		ALL("all", All.CODEC, All.STREAM_CODEC),
		NONE("none", None.CODEC, None.STREAM_CODEC),
		DETAILED("detailed", Detailed.CODEC, Detailed.STREAM_CODEC)
		;

		override fun getSerializedName(): String = this.id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Type> = StringRepresentable.fromEnum(Type::values)
			val STREAM_CODEC: StreamCodec<ByteBuf, Type> = ByteBufCodecs.fromCodec(CODEC)
		}
	}

	class All : ImprovedEntityPredicate {
		override fun matches(entity: Entity?) = true
		override fun getType(): Type = Type.ALL

		private constructor()

		companion object {
			val INSTANCE = All()

			val CODEC: MapCodec<All> = MapCodec.unit { INSTANCE }
			val STREAM_CODEC: StreamCodec<ByteBuf, All> = StreamCodec.unit(INSTANCE)
		}
	}

	class None(val noneOf: List<ImprovedEntityPredicate>) : ImprovedEntityPredicate {

		constructor(vararg noneOf: ImprovedEntityPredicate) : this(noneOf.toList())

		override fun matches(entity: Entity?): Boolean {
			return noneOf.none { it.matches(entity) }
		}

		override fun getType(): Type = Type.NONE

		companion object {
			val CODEC: MapCodec<None> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ImprovedEntityPredicate.CODEC
							.listOf()
							.fieldOf("none_of")
							.forGetter(None::noneOf)
					).apply(instance, ::None)
				}

			val STREAM_CODEC: StreamCodec<ByteBuf, None> =
				StreamCodec.composite(
					ImprovedEntityPredicate.STREAM_CODEC.apply(ByteBufCodecs.list()), None::noneOf,
					::None
				)
		}
	}

	class Detailed(
		val entityType: Optional<EntityTypePredicate> = Optional.empty(),
		val movement: Optional<MovementPredicate> = Optional.empty(),
		val effects: Optional<MobEffectsPredicate> = Optional.empty(),
		val nbt: Optional<NbtPredicate> = Optional.empty(),
		val flags: Optional<EntityFlagsPredicate> = Optional.empty(),
		val periodicTick: Optional<Int> = Optional.empty(),
		val slots: Optional<SlotsPredicate> = Optional.empty()
	) : ImprovedEntityPredicate {

		constructor(
			entityType: EntityTypePredicate? = null,
			movement: MovementPredicate? = null,
			effects: MobEffectsPredicate? = null,
			nbt: NbtPredicate? = null,
			flags: EntityFlagsPredicate? = null,
			periodicTick: Int? = null,
			slots: SlotsPredicate? = null
		) : this(
			Optional.ofNullable(entityType),
			Optional.ofNullable(movement),
			Optional.ofNullable(effects),
			Optional.ofNullable(nbt),
			Optional.ofNullable(flags),
			Optional.ofNullable(periodicTick),
			Optional.ofNullable(slots)
		)

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
						SlotsPredicate.CODEC
							.optionalFieldOf("slots")
							.forGetter(Detailed::slots)
					).apply(instance, ::Detailed)
				}

			val STREAM_CODEC: StreamCodec<ByteBuf, Detailed> =
				NeoForgeStreamCodecs.composite(
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(EntityTypePredicate.CODEC)), Detailed::entityType,
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(MovementPredicate.CODEC)), Detailed::movement,
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(MobEffectsPredicate.CODEC)), Detailed::effects,
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(NbtPredicate.CODEC)), Detailed::nbt,
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(EntityFlagsPredicate.CODEC)), Detailed::flags,
					ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), Detailed::periodicTick,
					ByteBufCodecs.optional(ByteBufCodecs.fromCodec(SlotsPredicate.CODEC)), Detailed::slots,
					::Detailed
				)
		}
	}

}
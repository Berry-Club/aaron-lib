package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.MovementSnapshot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EntityType
import java.util.*

class DetailedEntityPredicate(
	val entityType: Optional<EntityTypePredicate> = Optional.empty(),
	val movement: Optional<MovementPredicate> = Optional.empty(),
	val effects: Optional<MobEffectsPredicate> = Optional.empty(),
	val nbt: Optional<NbtPredicate> = Optional.empty(),
	val flags: Optional<EntityFlagsPredicate> = Optional.empty(),
//	val slots: Optional<SlotsPredicate> = Optional.empty()
) : EntityPredicate {

	constructor(
		entityType: EntityTypePredicate? = null,
		movement: MovementPredicate? = null,
		effects: MobEffectsPredicate? = null,
		nbt: NbtPredicate? = null,
		flags: EntityFlagsPredicate? = null,
//		slots: SlotsPredicate? = null
	) : this(
		Optional.ofNullable(entityType),
		Optional.ofNullable(movement),
		Optional.ofNullable(effects),
		Optional.ofNullable(nbt),
		Optional.ofNullable(flags),
//		Optional.ofNullable(slots)
	)

	fun getPassingSnapshot(): EntitySnapshot {
		var et: EntityType<*>? = null
		if (entityType.isPresent) {
			et = BuiltInRegistries.ENTITY_TYPE.first { entityType.get().matches(it) }
		}

		var movement: MovementSnapshot? = null
		if (this.movement.isPresent) {
			movement = MovementSnapshot.createFromPredicate(this.movement.get())
		}

	}

	override fun test(entitySnapshot: EntitySnapshot): Boolean {
		if (entityType.isPresent && !entityType.get().matches(entitySnapshot.entityType)) return false
		if (movement.isPresent && !entitySnapshot.movementSnapshot?.test(movement.get()).isTrue()) return false

		if (effects.isPresent && !effects.get().matches(entitySnapshot.activeEffects)) return false
		if (nbt.isPresent && !entitySnapshot.nbtSnapshot?.test(nbt.get()).isTrue()) return false
		if (flags.isPresent && !entitySnapshot.flagsSnapshot?.test(flags.get()).isTrue()) return false

//		if (slots.isPresent && !slots.get().matches(entitySnapshot)) return false

		return true
	}

	override fun getType(): EntityPredicate.Type = EntityPredicate.Type.DETAILED

	companion object {
		val CODEC: MapCodec<DetailedEntityPredicate> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					EntityTypePredicate.CODEC
						.optionalFieldOf("entity_type")
						.forGetter(DetailedEntityPredicate::entityType),
					MovementPredicate.CODEC
						.optionalFieldOf("movement")
						.forGetter(DetailedEntityPredicate::movement),
					MobEffectsPredicate.CODEC
						.optionalFieldOf("effects")
						.forGetter(DetailedEntityPredicate::effects),
					NbtPredicate.CODEC
						.optionalFieldOf("nbt")
						.forGetter(DetailedEntityPredicate::nbt),
					EntityFlagsPredicate.CODEC
						.optionalFieldOf("flags")
						.forGetter(DetailedEntityPredicate::flags),
//					SlotsPredicate.CODEC
//						.optionalFieldOf("slots")
//						.forGetter(DetailedEntityPredicate::slots)
				).apply(instance, ::DetailedEntityPredicate)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, DetailedEntityPredicate> =
			StreamCodec.composite(
				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(EntityTypePredicate.CODEC)), DetailedEntityPredicate::entityType,
				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(MovementPredicate.CODEC)), DetailedEntityPredicate::movement,
				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(MobEffectsPredicate.CODEC)), DetailedEntityPredicate::effects,
				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(NbtPredicate.CODEC)), DetailedEntityPredicate::nbt,
				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(EntityFlagsPredicate.CODEC)), DetailedEntityPredicate::flags,
//				ByteBufCodecs.optional(ByteBufCodecs.fromCodec(SlotsPredicate.CODEC)), DetailedEntityPredicate::slots,
				::DetailedEntityPredicate
			)
	}
}
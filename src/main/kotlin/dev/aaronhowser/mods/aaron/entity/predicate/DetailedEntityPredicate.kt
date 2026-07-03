package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.FlagsSnapshot
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.MovementSnapshot
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.NbtSnapshot
import dev.aaronhowser.mods.aaron.misc.BooleanExtensions.isTrue
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.criterion.*
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
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
			et = BuiltInRegistries.ENTITY_TYPE.first { entityType.get().matches(it.builtInRegistryHolder()) }
		}

		var movement: MovementSnapshot? = null
		if (this.movement.isPresent) {
			movement = MovementSnapshot.createFromPredicate(this.movement.get())
		}

		var nbt: NbtSnapshot? = null
		if (this.nbt.isPresent) {
			nbt = NbtSnapshot.fromPredicate(this.nbt.get())
		}

		var flags: FlagsSnapshot? = null
		if (this.flags.isPresent) {
			flags = FlagsSnapshot.fromPredicate(this.flags.get())
		}

		val effectsMap = mutableMapOf<Holder<MobEffect>, MobEffectInstance>()
		if (this.effects.isPresent) {
			val a = this.effects.get()

			for ((effect, predicate) in a.effectMap) {
				fun pick(bounds: MinMaxBounds.Ints): Int {
					if (bounds.isAny) return 0

					val hasMin = bounds.min().isPresent
					val hasMax = bounds.max().isPresent

					if (hasMin && hasMax) {
						val min = bounds.min().get()
						val max = bounds.max().get()

						return ((min + max) / 2.0).toInt()
					} else if (hasMin) {
						return bounds.min().get() + 1
					} else if (hasMax) {
						return bounds.max().get() - 1
					} else {
						return 0
					}
				}

				val duration = pick(predicate.duration)
				val amplifier = pick(predicate.amplifier)
				val ambient = predicate.ambient.orElse(false)
				val visible = predicate.visible.orElse(true)

				effectsMap[effect] = MobEffectInstance(
					effect,
					duration,
					amplifier,
					ambient,
					visible
				)
			}

		}

		return EntitySnapshot(
			entityType = et,
			nbtSnapshot = nbt,
			flagsSnapshot = flags,
			movementSnapshot = movement,
			activeEffects = effectsMap
		)
	}

	override fun test(entitySnapshot: EntitySnapshot): Boolean {
		if (entityType.isPresent && entitySnapshot.entityType != null && !entityType.get().matches(entitySnapshot.entityType.builtInRegistryHolder())) return false
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

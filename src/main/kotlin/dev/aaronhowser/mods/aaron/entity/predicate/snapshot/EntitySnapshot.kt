package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity

data class EntitySnapshot(
	val entityType: EntityType<*>,
	val nbtSnapshot: NbtSnapshot?,
	val flagsSnapshot: FlagsSnapshot?,
	val movementSnapshot: MovementSnapshot?,
	val activeEffects: Map<Holder<MobEffect>, MobEffectInstance> = emptyMap()
) {

	companion object {

		val CODEC: Codec<EntitySnapshot> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec()
						.fieldOf("entity_type")
						.forGetter(EntitySnapshot::entityType),
					NbtSnapshot.CODEC
						.optionalFieldOf("nbt", null)
						.forGetter(EntitySnapshot::nbtSnapshot),
					FlagsSnapshot.CODEC
						.optionalFieldOf("flags", null)
						.forGetter(EntitySnapshot::flagsSnapshot),
					MovementSnapshot.CODEC
						.optionalFieldOf("movement", null)
						.forGetter(EntitySnapshot::movementSnapshot),
					Codec.unboundedMap(
						BuiltInRegistries.MOB_EFFECT.holderByNameCodec(),
						MobEffectInstance.CODEC
					)
						.optionalFieldOf("active_effects", emptyMap())
						.forGetter(EntitySnapshot::activeEffects)
				).apply(instance, ::EntitySnapshot)
			}

		fun fromEntity(entity: Entity, includeNbtKeys: List<String>): EntitySnapshot {
			val nbtSnapshot = NbtSnapshot.fromEntity(entity, includeNbtKeys)

			val flagsSnapshot = FlagsSnapshot(
				isOnGround = entity.onGround(),
				isOnFire = entity.isOnFire,
				isCrouching = entity.isCrouching,
				isSprinting = entity.isSprinting,
				isSwimming = entity.isSwimming
			)

			val movementSnapshot = MovementSnapshot(
				deltaMovement = entity.deltaMovement.scale(20.0),
				fallDistance = entity.fallDistance.toDouble()
			)

			return EntitySnapshot(
				entity.type,
				nbtSnapshot,
				flagsSnapshot,
				movementSnapshot,
				if (entity is LivingEntity) entity.activeEffectsMap else emptyMap()
			)
		}
	}

}
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
import java.util.*

data class EntitySnapshot(
	val entityType: EntityType<*>,
	val nbtSnapshot: Optional<NbtSnapshot>,
	val flagsSnapshot: Optional<FlagsSnapshot>,
	val movementSnapshot: Optional<MovementSnapshot>,
	val activeEffects: Map<Holder<MobEffect>, MobEffectInstance>
) {

	companion object {

		val CODEC: Codec<EntitySnapshot> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec()
						.fieldOf("entity_type")
						.forGetter(EntitySnapshot::entityType),
					NbtSnapshot.CODEC
						.optionalFieldOf("nbt")
						.forGetter(EntitySnapshot::nbtSnapshot),
					FlagsSnapshot.CODEC
						.optionalFieldOf("flags")
						.forGetter(EntitySnapshot::flagsSnapshot),
					MovementSnapshot.CODEC
						.optionalFieldOf("movement")
						.forGetter(EntitySnapshot::movementSnapshot),
					MobEffectInstance.MAP_CODEC
						.fieldOf("active_effects")
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
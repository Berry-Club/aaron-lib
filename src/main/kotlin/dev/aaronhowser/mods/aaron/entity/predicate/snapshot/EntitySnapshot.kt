package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity

data class EntitySnapshot(
	val entityType: EntityType<*>,
	val tickCount: Int,
	val nbtSnapshot: NbtSnapshot,
	val flagsSnapshot: FlagsSnapshot,
	val movementSnapshot: MovementSnapshot,
	val activeEffects: Map<Holder<MobEffect>, MobEffectInstance>
) {

	companion object {
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
				entity.tickCount,
				nbtSnapshot,
				flagsSnapshot,
				movementSnapshot,
				if (entity is LivingEntity) entity.activeEffectsMap else emptyMap()
			)
		}
	}

}
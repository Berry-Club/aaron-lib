package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

data class EntitySnapshot(
	val entityType: EntityType<*>,
	val nbtSnapshot: NbtSnapshot,
	val flagsSnapshot: FlagsSnapshot,
	val movementSnapshot: MovementSnapshot
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
				deltaMovement = entity.deltaMovement,
				fallDistance = entity.fallDistance.toDouble()
			)

			return EntitySnapshot(entity.type, nbtSnapshot, flagsSnapshot, movementSnapshot)
		}
	}

}
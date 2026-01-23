package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.world.entity.Entity

data class EntitySnapshot(
	val nbtSnapshot: NbtSnapshot,
	val flagsSnapshot: FlagsSnapshot
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

			return EntitySnapshot(nbtSnapshot, flagsSnapshot)
		}
	}

}
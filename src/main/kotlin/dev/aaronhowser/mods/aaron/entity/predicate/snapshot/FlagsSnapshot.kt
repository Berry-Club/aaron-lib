package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.advancements.critereon.EntityFlagsPredicate

data class FlagsSnapshot(
	val isOnGround: Boolean,
	val isOnFire: Boolean,
	val isCrouching: Boolean,
	val isSprinting: Boolean,
	val isSwimming: Boolean,
) {

	fun test(predicate: EntityFlagsPredicate): Boolean {
		if (predicate.isOnGround.isPresent && predicate.isOnGround.get() != isOnGround) return false
		if (predicate.isOnFire.isPresent && predicate.isOnFire.get() != isOnFire) return false
		if (predicate.isCrouching.isPresent && predicate.isCrouching.get() != isCrouching) return false
		if (predicate.isSprinting.isPresent && predicate.isSprinting.get() != isSprinting) return false
		if (predicate.isSwimming.isPresent && predicate.isSwimming.get() != isSwimming) return false
		return true
	}

}
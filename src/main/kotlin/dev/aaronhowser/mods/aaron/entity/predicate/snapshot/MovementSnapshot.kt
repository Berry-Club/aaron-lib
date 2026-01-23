package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.advancements.critereon.MovementPredicate
import net.minecraft.world.phys.Vec3

class MovementSnapshot(
	val deltaMovement: Vec3,
	val fallDistance: Double
) {

	fun test(predicate: MovementPredicate): Boolean {
		return predicate.matches(
			deltaMovement.x,
			deltaMovement.y,
			deltaMovement.z,
			fallDistance
		)
	}

}
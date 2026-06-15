package dev.aaronhowser.mods.aaron.actor

import net.minecraft.world.level.Level

abstract class LevelActor(
	val level: Level
) {

	var isRemoved: Boolean = false
		private set

	fun remove() {
		isRemoved = true
	}

	abstract fun tick()

}
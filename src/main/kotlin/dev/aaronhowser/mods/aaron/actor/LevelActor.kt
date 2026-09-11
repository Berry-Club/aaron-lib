package dev.aaronhowser.mods.aaron.actor

import dev.aaronhowser.mods.aaron.LevelActorHolder
import net.minecraft.world.level.Level

abstract class LevelActor(
	val level: Level
) {

	var isRemoved: Boolean = false
		private set

	var age: Int = 0
		protected set

	fun markForRemoval() {
		isRemoved = true
	}

	private fun fullTick() {
		if (age == 0) setup()
		age++
		tick()
	}

	protected open fun setup() {}
	protected abstract fun tick()

	companion object {
		fun Level.getLevelActors(): MutableList<LevelActor> = (this as LevelActorHolder).`aaron$getLevelActors`()

		fun Level.addLevelActor(actor: LevelActor) {
			getLevelActors().add(actor)
		}

		fun tickActors(level: Level) {
			val iterator = level.getLevelActors().iterator()

			while (iterator.hasNext()) {
				val actor = iterator.next()

				actor.fullTick()

				if (actor.isRemoved) {
					iterator.remove()
				}
			}
		}
	}

}
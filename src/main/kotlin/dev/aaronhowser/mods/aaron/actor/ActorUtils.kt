package dev.aaronhowser.mods.aaron.actor

import dev.aaronhowser.mods.aaron.LevelActorHolder
import net.minecraft.world.level.Level

object ActorUtils {

	fun Level.getLevelActors(): MutableList<LevelActor> = (this as LevelActorHolder).`aaron$getLevelActors`()

	fun Level.addLevelActor(actor: LevelActor) {
		getLevelActors().add(actor)
	}

	fun tickActors(level: Level) {
		val iterator = level.getLevelActors().iterator()

		while (iterator.hasNext()) {
			val actor = iterator.next()

			actor.tick()
			if (actor.isRemoved) {
				iterator.remove()
			}
		}
	}

}
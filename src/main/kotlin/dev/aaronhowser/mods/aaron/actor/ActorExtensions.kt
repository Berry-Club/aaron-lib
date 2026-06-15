package dev.aaronhowser.mods.aaron.actor

import dev.aaronhowser.mods.aaron.LevelActorHolder
import net.minecraft.world.level.Level

object ActorExtensions {

	fun Level.getLevelActors(): List<LevelActor> = (this as LevelActorHolder).`aaron$getLevelActors`()
	fun Level.addLevelActor(actor: LevelActor) = (this as LevelActorHolder).`aaron$addLevelActor`(actor)

}
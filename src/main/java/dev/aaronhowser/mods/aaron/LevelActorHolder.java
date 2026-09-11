package dev.aaronhowser.mods.aaron;

import dev.aaronhowser.mods.aaron.actor.LevelActor;

import java.util.List;

public interface LevelActorHolder {

	default List<LevelActor> aaron$getLevelActors() {
		throw new IllegalStateException();
	}

}

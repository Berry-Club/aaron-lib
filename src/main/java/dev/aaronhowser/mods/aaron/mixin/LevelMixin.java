package dev.aaronhowser.mods.aaron.mixin;

import dev.aaronhowser.mods.aaron.LevelActorHolder;
import dev.aaronhowser.mods.aaron.SchedulerHolder;
import dev.aaronhowser.mods.aaron.actor.LevelActor;
import dev.aaronhowser.mods.aaron.scheduler.ScheduledTaskHandler;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Level.class)
public abstract class LevelMixin implements SchedulerHolder, LevelActorHolder {

	@Unique
	private ScheduledTaskHandler aaron$scheduledTaskHandler;

	@Unique
	private final List<LevelActor> aaron$levelActors = new ArrayList<>();

	@Override
	public ScheduledTaskHandler aaron$getScheduledTaskHandler() {
		if (aaron$scheduledTaskHandler == null) {
			aaron$scheduledTaskHandler = new ScheduledTaskHandler(((Level) (Object) this)::getGameTime);
		}

		return aaron$scheduledTaskHandler;
	}

	@Override
	public @Nullable ScheduledTaskHandler aaron$getScheduledTaskHandlerRaw() {
		return aaron$scheduledTaskHandler;
	}

	@Override
	public List<LevelActor> aaron$getLevelActors() {
		return aaron$levelActors;
	}

}

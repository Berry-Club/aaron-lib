package dev.aaronhowser.mods.aaron.mixin;

import dev.aaronhowser.mods.aaron.SchedulerHolder;
import dev.aaronhowser.mods.aaron.scheduler.ScheduledTaskHandler;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public abstract class LevelMixin implements SchedulerHolder {

	@Unique
	private ScheduledTaskHandler aaron$scheduledTaskHandler;

	@Shadow
	public abstract long getGameTime();

	@Override
	public ScheduledTaskHandler aaron$getScheduledTaskHandler() {
		if (aaron$scheduledTaskHandler == null) {
			aaron$scheduledTaskHandler = new ScheduledTaskHandler(this::getGameTime);
		}

		return aaron$scheduledTaskHandler;
	}

	@Override
	public @Nullable ScheduledTaskHandler aaron$getScheduledTaskHandlerRaw() {
		return aaron$scheduledTaskHandler;
	}
}

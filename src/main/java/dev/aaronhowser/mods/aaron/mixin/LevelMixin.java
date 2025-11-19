package dev.aaronhowser.mods.aaron.mixin;

import dev.aaronhowser.mods.aaron.SchedulerHolder;
import dev.aaronhowser.mods.aaron.scheduler.ScheduledTaskHandler;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public abstract class LevelMixin implements SchedulerHolder {

	@Shadow
	public abstract long getGameTime();

	@Unique
	private ScheduledTaskHandler aaron$scheduledTaskHandler;

	@Override
	public ScheduledTaskHandler aaron$getScheduledTaskHandler() {
		if (aaron$scheduledTaskHandler == null) {
			aaron$scheduledTaskHandler = new ScheduledTaskHandler(this::getGameTime);
		}

		return aaron$scheduledTaskHandler;
	}
}

package dev.aaronhowser.mods.aaron;

import dev.aaronhowser.mods.aaron.scheduler.ScheduledTaskHandler;

import javax.annotation.Nullable;

public interface SchedulerHolder {

	default ScheduledTaskHandler aaron$getScheduledTaskHandler() {
		throw new IllegalStateException();
	}

	@Nullable
	default ScheduledTaskHandler aaron$getScheduledTaskHandlerRaw() {
		throw new IllegalStateException();
	}

}

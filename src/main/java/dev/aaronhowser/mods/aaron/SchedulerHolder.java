package dev.aaronhowser.mods.aaron;

import dev.aaronhowser.mods.aaron.scheduler.ScheduledTaskHandler;

public interface SchedulerHolder {

	default ScheduledTaskHandler aaron$getScheduledTaskHandler() {
		throw new IllegalStateException();
	}

}

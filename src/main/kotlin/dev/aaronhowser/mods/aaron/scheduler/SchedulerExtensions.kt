package dev.aaronhowser.mods.aaron.scheduler

import dev.aaronhowser.mods.aaron.SchedulerHolder
import net.minecraft.world.level.Level

object SchedulerExtensions {

	fun Level.getScheduler(): ScheduledTaskHandler = (this as SchedulerHolder).`aaron$getScheduledTaskHandler`()
	fun Level.getSchedulerRaw(): ScheduledTaskHandler? = (this as SchedulerHolder).`aaron$getScheduledTaskHandlerRaw`()

	fun Level.scheduleTaskInTicks(delay: Int, task: Runnable) {
		this.getScheduler().run(delay, RepeatingTask.WrappedRunnable(task))
	}

}
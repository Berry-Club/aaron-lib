package dev.aaronhowser.mods.aaron.scheduler

import org.apache.commons.lang3.mutable.MutableInt

class ScheduledTask(
	val handler: ScheduledTaskHandler,
	val task: RepeatingTask,
	val at: Long
) {

	val currentTick: MutableInt = MutableInt(0)

	/**
	 * @return true if the task is complete
	 */
	fun tick(): Boolean {
		if (handler.levelTime.asLong < at) {
			return false
		}

		val tick = currentTick.incrementAndGet()
		return !task.run(tick)
	}

}
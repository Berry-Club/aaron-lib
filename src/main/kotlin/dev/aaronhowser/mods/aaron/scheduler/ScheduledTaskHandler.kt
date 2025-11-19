package dev.aaronhowser.mods.aaron.scheduler

import java.util.*
import java.util.function.LongSupplier

class ScheduledTaskHandler(
	val levelTime: LongSupplier
) {

	val tasks: LinkedList<ScheduledTask> = LinkedList()
	val newTasks: MutableList<ScheduledTask> = mutableListOf()

	fun run(delay: Int, task: RepeatingTask) {
		val st = ScheduledTask(this, task, levelTime.asLong + delay)

		if (delay > 0 && !st.tick()) {
			newTasks.add(st)
		}
	}

	fun tick() {
		if (newTasks.isNotEmpty()) {
			tasks.addAll(newTasks)
			newTasks.clear()
		}

		tasks.removeIf(ScheduledTask::tick)
	}

}
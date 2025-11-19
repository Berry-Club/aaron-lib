package dev.aaronhowser.mods.aaron.scheduler

fun interface RepeatingTask {

	/**
	 * @param ticksRunning The number of ticks this task has been running for
	 * @return true if the task is complete
	 */
	fun run(ticksRunning: Int): Boolean

	class WrappedRunnable(
		private val runnable: Runnable
	) : RepeatingTask {
		override fun run(ticksRunning: Int): Boolean {
			runnable.run()
			return false
		}
	}

}
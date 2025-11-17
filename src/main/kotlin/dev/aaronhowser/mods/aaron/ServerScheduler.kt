package dev.aaronhowser.mods.aaron

import com.google.common.collect.HashMultimap
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent

@EventBusSubscriber(
	modid = AaronLib.MOD_ID
)
object ServerScheduler {

	@SubscribeEvent
	fun afterServerTick(event: ServerTickEvent.Post) {
		currentTick++
	}

	private var currentTick = 0
		set(value) {
			field = value
			handleScheduledTasks(value)
		}

	private val upcomingTasks: HashMultimap<Int, Runnable> = HashMultimap.create()

	fun scheduleTaskInTicks(ticksInFuture: Int, runnable: Runnable) {
		if (ticksInFuture > 0) {
			upcomingTasks.put(currentTick + ticksInFuture, runnable)
		} else {
			runnable.run()
		}
	}

	private fun handleScheduledTasks(tick: Int) {
		if (!upcomingTasks.containsKey(tick)) return

		val tasks = upcomingTasks[tick].iterator()

		while (tasks.hasNext()) {
			try {
				tasks.next().run()
			} catch (e: Exception) {
				AaronLib.LOGGER.error(e.toString())
			}

			tasks.remove()
		}
	}

}
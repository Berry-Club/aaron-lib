package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.getSchedulerRaw
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = AaronLib.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.FORGE
)
object AaronCommonForgeBusEvents {

	@SubscribeEvent
	fun onPlayerLoggedIn(event: PlayerEvent.PlayerLoggedInEvent) {
		val name = event.entity.gameProfile.name
		if (name.lowercase().contains("aaron")) {
			AaronLib.LOGGER.info("Aaron spotted!!!!")
		}
	}

	@SubscribeEvent
	fun onLevelTick(event: TickEvent.LevelTickEvent) {
		if (event.phase == TickEvent.Phase.END) {
			event.level.getSchedulerRaw()?.tick()
		}
	}
}
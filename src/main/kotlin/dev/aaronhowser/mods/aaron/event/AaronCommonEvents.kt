package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.AaronPacketRegister
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.getScheduler
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(modid = AaronLib.MOD_ID)
object AaronCommonEvents {

	@SubscribeEvent
	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		AaronPacketRegister.registerPayloads(event)
	}

	@SubscribeEvent
	fun onPlayerLoggedIn(event: PlayerEvent.PlayerLoggedInEvent) {
		val name = event.entity.gameProfile.name
		if (name.lowercase().contains("aaron")) {
			AaronLib.LOGGER.info("Aaron spotted!!!!")
		}
	}

	@SubscribeEvent
	fun afterLevelTick(event: LevelTickEvent.Post) {
		event.level.getScheduler().tick()
	}

}
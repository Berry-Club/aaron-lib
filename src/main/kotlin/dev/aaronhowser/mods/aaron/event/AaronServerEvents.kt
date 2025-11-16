package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@EventBusSubscriber(modid = AaronLib.MOD_ID)
object AaronServerEvents {

	@SubscribeEvent
	fun onPlayerLoggedIn(event: PlayerEvent.PlayerLoggedInEvent) {
		val name = event.entity.gameProfile.name
		if (name.lowercase().contains("aaron")) {
			AaronLib.LOGGER.info("Aaron spotted!!!!")
		}
	}

}
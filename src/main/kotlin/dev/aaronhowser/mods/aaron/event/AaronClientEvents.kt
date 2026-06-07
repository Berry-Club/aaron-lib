package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent

@EventBusSubscriber(modid = AaronLib.MOD_ID)
object AaronClientEvents {

	@SubscribeEvent
	fun onRegisterClientReloadListeners(event: RegisterClientReloadListenersEvent) {
		event.registerReloadListener(ResourceManagerReloadListener {
			AaronClientUtil.FLUID_COLORS.clear()
		})
	}

}
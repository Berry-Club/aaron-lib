package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent

@EventBusSubscriber(modid = AaronLib.MOD_ID)
object AaronClientEvents {

	@SubscribeEvent
	fun onRegisterRenderPipelines(event: RegisterRenderPipelinesEvent) {
		event.registerPipeline(AaronRenderTypes.LINES_THROUGH_WALLS_PIPELINE)
		event.registerPipeline(AaronRenderTypes.QUADS_THROUGH_WALLS_PIPELINE)
	}

	@SubscribeEvent
	fun onRegisterClientReloadListeners(event: AddClientReloadListenersEvent) {
		event.addListener(
			AaronLib.modResource("fluid_colors"),
			ResourceManagerReloadListener {
				AaronClientUtil.FLUID_COLORS.clear()
			}
		)
	}

}
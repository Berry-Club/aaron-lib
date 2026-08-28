package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.command.AaronCommands
import dev.aaronhowser.mods.aaron.packet.AaronPacketRegister
import dev.aaronhowser.mods.aaron.registry.actual.AaronCriterionTriggers
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.getSchedulerRaw
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(modid = AaronLib.MOD_ID)
object AaronCommonEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onBlockBroken(event: BlockEvent.BreakEvent) {
		if (event.isCanceled) return

		val player = event.player as? ServerPlayer ?: return
		val level = event.level as? ServerLevel ?: return

		AaronCriterionTriggers.BLOCK_BROKEN.get().trigger(player, level, event.pos)
	}

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
		val level = event.level

		level.getSchedulerRaw()?.tick()
		LevelActor.tickActors(level)
	}

	@SubscribeEvent
	fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
		AaronCommands.register(event.dispatcher)
	}

}
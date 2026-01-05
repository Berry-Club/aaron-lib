package dev.aaronhowser.mods.aaron.event

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.command.AaronCommands
import dev.aaronhowser.mods.aaron.packet.AaronLibPacketRegistrar
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@Mod.EventBusSubscriber(
	modid = AaronLib.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
object AaronCommonModBusEvents {

	@SubscribeEvent
	fun registerPackets(event: FMLCommonSetupEvent) {
		AaronLibPacketRegistrar.registerPackets(event)
	}

	@SubscribeEvent
	fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
		AaronCommands.register(event.dispatcher)
	}

}
package dev.aaronhowser.mods.aaron.packet

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.c2s.ClientChangedMenuString
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.aaron.packet.s2c.UpdateClientScreenString
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel

object AaronPacketRegister : AaronPacketRegistrar {

	private const val PROTOCOL_VERSION = "1"

	val CHANNEL: SimpleChannel =
		NetworkRegistry.newSimpleChannel(
			AaronLib.modResource("main"),
			{ PROTOCOL_VERSION },
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		)

	fun registerPackets() {
		
	}

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1")

		toServer(
			registrar,
			ClientChangedMenuString.TYPE,
			ClientChangedMenuString.STREAM_CODEC
		)

		toServer(
			registrar,
			ClientClickedMenuButton.TYPE,
			ClientClickedMenuButton.STREAM_CODEC
		)

		toClient(
			registrar,
			UpdateClientScreenString.TYPE,
			UpdateClientScreenString.STREAM_CODEC
		)
	}

}
package dev.aaronhowser.mods.aaron.packet

import dev.aaronhowser.mods.aaron.packet.c2s.ClientChangedMenuString
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.aaron.packet.s2c.UpdateClientScreenString
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object AaronPacketRegister : AaronPacketRegistrar {

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
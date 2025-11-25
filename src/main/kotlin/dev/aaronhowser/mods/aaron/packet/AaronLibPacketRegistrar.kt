package dev.aaronhowser.mods.aaron.packet

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.c2s.ClientChangedMenuString
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.aaron.packet.s2c.UpdateClientScreenString
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel

object AaronLibPacketRegistrar : AaronPacketRegistrar() {

	private const val PROTOCOL_VERSION = "1"

	val CHANNEL: SimpleChannel =
		NetworkRegistry.newSimpleChannel(
			AaronLib.modResource("main"),
			{ PROTOCOL_VERSION },
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		)

	override fun getChannel(): SimpleChannel = CHANNEL

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	override fun registerPackets(event: FMLCommonSetupEvent) {
		var i = 0

		CHANNEL.registerMessage(
			++i,
			ClientChangedMenuString::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> ClientChangedMenuString.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) }
		)

		CHANNEL.registerMessage(
			++i,
			ClientClickedMenuButton::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> ClientClickedMenuButton.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) }
		)

		CHANNEL.registerMessage(
			++i,
			UpdateClientScreenString::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> UpdateClientScreenString.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) }
		)

	}

}
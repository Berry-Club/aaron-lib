package dev.aaronhowser.mods.aaron.packet

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.registration.PayloadRegistrar

interface AaronPacketRegistrar {

	fun <T : AaronPacket> toClient(
		registrar: PayloadRegistrar,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
	) {
		registrar.playToClient(
			packetType,
			streamCodec
		) { packet, context -> packet.receiveOnClient(context) }
	}

	fun <T : AaronPacket> toServer(
		registrar: PayloadRegistrar,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	) {
		registrar.playToServer(
			packetType,
			streamCodec
		) { packet, context -> packet.receiveOnServer(context) }
	}

}
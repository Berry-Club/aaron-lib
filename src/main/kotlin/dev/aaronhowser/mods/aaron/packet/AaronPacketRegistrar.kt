package dev.aaronhowser.mods.aaron.packet

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraftforge.network.simple.SimpleChannel
import net.neoforged.neoforge.network.registration.PayloadRegistrar

interface AaronPacketRegistrar {

	fun <T : AaronPacket> toClient(
		channel: SimpleChannel,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
	) {
	}

	fun <T : AaronPacket> toServer(
		channel: SimpleChannel,
		packetType: CustomPacketPayload.Type<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	) {

	}

}
package dev.aaronhowser.mods.aaron.packet

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel

abstract class AaronPacketRegistrar {

	abstract fun getChannel(): SimpleChannel

	fun messageServer(packet: AaronPacket) {
		getChannel().sendToServer(packet)
	}

	fun messagePlayer(packet: AaronPacket, player: ServerPlayer) {
		getChannel().send(PacketDistributor.PLAYER.with { player }, packet)
	}

	fun messageAllPlayers(packet: AaronPacket) {
		getChannel().send(PacketDistributor.ALL.noArg(), packet)
	}

	fun messageNearbyPlayers(packet: AaronPacket, pos: BlockPos, radius: Number) {
		messageNearbyPlayers(packet, pos.center, radius)
	}

	fun messageNearbyPlayers(packet: AaronPacket, pos: Vec3, radius: Number) {
		getChannel().send(
			PacketDistributor.NEAR.with {
				PacketDistributor.TargetPoint(
					pos.x,
					pos.y,
					pos.z,
					radius.toDouble(),
					null
				)
			},
			packet
		)
	}

	fun messageAllPlayersTrackingChunk(packet: AaronPacket, levelChunk: LevelChunk) {
		getChannel().send(
			PacketDistributor.TRACKING_CHUNK.with { levelChunk },
			packet
		)
	}

	fun messageAllPlayersTrackingEntity(packet: AaronPacket, entity: Entity) {
		getChannel().send(
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with { entity },
			packet
		)
	}

	fun messageAllPlayersTrackingEntityAndSelf(packet: AaronPacket, entity: Entity) {
		getChannel().send(
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with { entity },
			packet
		)
	}

}
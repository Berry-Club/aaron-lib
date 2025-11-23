package dev.aaronhowser.mods.aaron.packet

import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkEvent
import net.neoforged.neoforge.network.PacketDistributor
import java.util.function.Supplier

abstract class AaronPacket {

	abstract fun encode(buffer: FriendlyByteBuf)

	protected open fun handleOnClient(context: NetworkEvent.Context) {
		throw UnsupportedOperationException("Packet $this cannot be received on the client!")
	}

	protected open fun handleOnServer(context: NetworkEvent.Context) {
		throw UnsupportedOperationException("Packet $this cannot be received on the server!")
	}

	fun receiveOnClient(ctx: Supplier<NetworkEvent.Context>) {
		val context = ctx.get()
		context.enqueueWork {
			handleOnClient(context)
		}
	}

	fun receiveOnServer(ctx: Supplier<NetworkEvent.Context>) {
		val context = ctx.get()
		context.enqueueWork {
			handleOnServer(context)
		}
	}

	fun messagePlayer(player: ServerPlayer) = PacketDistributor.sendToPlayer(player, this)
	fun messageAllPlayers() = PacketDistributor.sendToAllPlayers(this)
	fun messageServer() = PacketDistributor.sendToServer(this)
	fun messageNearbyPlayers(serverLevel: ServerLevel, pos: BlockPos, radius: Double) = messageNearbyPlayers(serverLevel, pos.center, radius)
	fun messageNearbyPlayers(serverLevel: ServerLevel, origin: Vec3, radius: Double) = PacketDistributor.sendToPlayersNear(serverLevel, null, origin.x, origin.y, origin.z, radius, this)
	fun messageAllPlayersTrackingChunk(serverLevel: ServerLevel, chunkPos: ChunkPos) = PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunkPos, this)
	fun messageAllPlayersTrackingEntity(entity: Entity) = PacketDistributor.sendToPlayersTrackingEntity(entity, this)
	fun messageAllPlayersTrackingEntityAndSelf(entity: Entity) = PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, this)

}
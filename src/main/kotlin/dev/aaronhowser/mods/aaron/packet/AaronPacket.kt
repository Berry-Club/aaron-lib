package dev.aaronhowser.mods.aaron.packet

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
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

}
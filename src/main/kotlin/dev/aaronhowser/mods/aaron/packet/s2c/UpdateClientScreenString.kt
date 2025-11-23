package dev.aaronhowser.mods.aaron.packet.s2c

import dev.aaronhowser.mods.aaron.menu.ScreenWithStrings
import dev.aaronhowser.mods.aaron.packet.AaronPacket
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class UpdateClientScreenString(
	val stringId: Int,
	val newString: String
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeVarInt(stringId)
		buffer.writeUtf(newString)
	}

	override fun handleOnClient(context: NetworkEvent.Context) {
		val screen = Minecraft.getInstance().screen ?: return

		if (screen is ScreenWithStrings) {
			screen.receivedString(stringId, newString)
		}
	}

}
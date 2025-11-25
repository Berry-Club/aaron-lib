package dev.aaronhowser.mods.aaron.packet.c2s

import dev.aaronhowser.mods.aaron.menu.MenuWithStrings
import dev.aaronhowser.mods.aaron.packet.AaronPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class ClientChangedMenuString(
	val stringId: Int,
	val newString: String
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeVarInt(stringId)
		buffer.writeUtf(newString)
	}

	override fun handleOnServer(context: NetworkEvent.Context) {
		val player = context.sender ?: return

		val playerMenu = player.containerMenu
		if (playerMenu is MenuWithStrings) {
			playerMenu.receiveString(stringId, newString)
		}
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): ClientChangedMenuString {
			val stringId = buffer.readVarInt()
			val newString = buffer.readUtf()
			return ClientChangedMenuString(stringId, newString)
		}
	}

}
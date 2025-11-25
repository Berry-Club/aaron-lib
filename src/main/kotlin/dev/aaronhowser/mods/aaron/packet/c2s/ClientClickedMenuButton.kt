package dev.aaronhowser.mods.aaron.packet.c2s

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.aaron.packet.AaronPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class ClientClickedMenuButton(
	private val buttonId: Int
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeVarInt(buttonId)
	}

	override fun handleOnServer(context: NetworkEvent.Context) {
		val player = context.sender ?: return

		val playerMenu = player.containerMenu
		if (!playerMenu.stillValid(player)) return

		if (playerMenu is MenuWithButtons) {
			playerMenu.handleButtonPressed(buttonId)
		}
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): ClientClickedMenuButton {
			val buttonId = buffer.readVarInt()
			return ClientClickedMenuButton(buttonId)
		}
	}

}
package dev.aaronhowser.mods.aaron.packet.c2s

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.ModPacket
import dev.aaronhowser.mods.irregular_implements.menu.MenuWithButtons
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientClickedMenuButton(
	private val buttonId: Int
) : ModPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		val player = context.player() as? ServerPlayer ?: return
		val playerMenu = player.containerMenu

		if (!playerMenu.stillValid(player)) return

		if (playerMenu is MenuWithButtons) {
			playerMenu.handleButtonPressed(buttonId)
		}
	}

	override fun type(): CustomPacketPayload.Type<ClientClickedMenuButton> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<ClientClickedMenuButton> =
			CustomPacketPayload.Type(AaronLib.modResource("client_clicked_menu_button"))

		val STREAM_CODEC: StreamCodec<ByteBuf, ClientClickedMenuButton> =
			StreamCodec.composite(
				ByteBufCodecs.VAR_INT, ClientClickedMenuButton::buttonId,
				::ClientClickedMenuButton
			)
	}
}
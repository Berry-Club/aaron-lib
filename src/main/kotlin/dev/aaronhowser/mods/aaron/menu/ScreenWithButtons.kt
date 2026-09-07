package dev.aaronhowser.mods.aaron.menu

import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton

interface ScreenWithButtons {

	fun sendMenuButtonClick(menuButtonId: Int, mouseButton: Int, shiftDown: Boolean) {
		ClientClickedMenuButton(
			menuButtonId = menuButtonId,
			mouseButton = mouseButton,
			shiftDown = shiftDown
		).messageServer()
	}

}
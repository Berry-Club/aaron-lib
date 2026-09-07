package dev.aaronhowser.mods.aaron.menu

interface MenuWithButtons {

	fun handleButtonPressed(menuButtonId: Int, mouseButton: Int, shiftDown: Boolean)

}
package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import java.util.function.Supplier

open class ChangingTextButton(
	x: Int,
	y: Int,
	width: Int,
	height: Int,
	private val messageGetter: Supplier<Component>,
	private val font: Font,
	onPress: OnPress
) : Button(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION) {

	override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
		graphics.text(
			font,
			message,
			x,
			y,
			16777215
		)
	}

	override fun getMessage(): Component {
		return this.messageGetter.get()
	}

}
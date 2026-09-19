package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

open class TexturedLabel(
	x: Int,
	y: Int,
	private val font: Font,
	private val messageGetter: Supplier<Component>,
	private val backgroundSprite: ResourceLocation,
	private val horizontalPadding: Int = 6,
	private val verticalPadding: Int = 4,
	private val textColor: Int = 0xFFFFFF,
	private val drawShadow: Boolean = false,
	private val minimumWidth: Int = 0,
	private val minimumHeight: Int = 0
) : AbstractWidget(x, y, 0, 0, Component.empty()) {

	init {
		active = false
		updateDimensions(getMessage())
	}

	override fun getMessage(): Component {
		return messageGetter.get()
	}

	override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		val currentMessage = getMessage()
		updateDimensions(currentMessage)

		guiGraphics.blitSprite(
			backgroundSprite,
			x,
			y,
			width,
			height
		)

		val textX = x + (width - font.width(currentMessage)) / 2
		val textY = y + (height - font.lineHeight) / 2

		guiGraphics.drawString(
			font,
			currentMessage,
			textX,
			textY,
			textColor,
			drawShadow
		)
	}

	override fun updateWidgetNarration(narrationOutput: NarrationElementOutput) {
		narrationOutput.add(NarratedElementType.TITLE, getMessage())
	}

	private fun updateDimensions(currentMessage: Component) {
		width = maxOf(font.width(currentMessage) + horizontalPadding * 2, minimumWidth)
		height = maxOf(font.lineHeight + verticalPadding * 2, minimumHeight)
	}

}
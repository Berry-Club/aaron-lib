package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import java.util.function.Supplier

open class TexturedLabel(
	x: Int,
	y: Int,
	private val font: Font,
	private val messageGetter: Supplier<Component>,
	private val backgroundSprite: Identifier,
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

	override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
		val currentMessage = getMessage()
		updateDimensions(currentMessage)

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			backgroundSprite,
			x,
			y,
			width,
			height
		)

		val textX = x + (width - font.width(currentMessage)) / 2
		val textY = y + (height - font.lineHeight) / 2

		graphics.text(
			font,
			currentMessage,
			textX,
			textY,
			getRenderableTextColor(),
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

	private fun getRenderableTextColor(): Int {
		if (textColor and 0xFC000000.toInt() == 0) return ARGB.opaque(textColor)

		return textColor
	}

}
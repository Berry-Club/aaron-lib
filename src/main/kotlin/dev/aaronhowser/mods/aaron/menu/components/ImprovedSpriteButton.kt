package dev.aaronhowser.mods.aaron.menu.components

import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

open class ImprovedSpriteButton(
	x: Int = 0,
	y: Int = 0,
	width: Int,
	height: Int,
	private val spriteWidth: Int,
	private val spriteHeight: Int,
	private val sprite: Identifier,
	onPress: OnPress,
	message: Component = Component.empty(),
	private val font: Font
) : Button(x, y, width, height, message, onPress, DEFAULT_NARRATION) {

	constructor(
		x: Int,
		y: Int,
		width: Int,
		height: Int,
		menuSprite: ScreenSprite,
		onPress: OnPress,
		message: Component = Component.empty(),
		font: Font
	) : this(x, y, width, height, menuSprite.width, menuSprite.height, menuSprite.texture, onPress, message, font)

	override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
		extractDefaultSprite(graphics)

		val i = this.x + this.getWidth() / 2 - this.spriteWidth / 2
		val j = this.y + this.getHeight() / 2 - this.spriteHeight / 2
		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			sprite,
			i,
			j,
			this.spriteWidth,
			this.spriteHeight
		)

		if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
			renderToolTip(graphics, mouseX, mouseY)
		}
	}

	private fun renderToolTip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		if (this.message == Component.empty()) return

		graphics.setComponentTooltipForNextFrame(
			font,
			listOf(this.message),
			mouseX,
			mouseY
		)
	}

}

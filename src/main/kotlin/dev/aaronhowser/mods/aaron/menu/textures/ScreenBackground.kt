package dev.aaronhowser.mods.aaron.menu.textures

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

open class ScreenBackground(
	private val texture: Identifier,
	val width: Int,
	val height: Int,
	private val canvasSize: Int = 256
) {
	fun render(graphics: GuiGraphicsExtractor, leftPos: Int, topPos: Int) {
		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			this.texture,
			this.canvasSize,
			this.canvasSize,
			0,
			0,
			leftPos,
			topPos,
			this.width,
			this.height
		)
	}
}

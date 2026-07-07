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
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			this.texture,
			leftPos,
			topPos,
			0f,
			0f,
			this.width,
			this.height,
			this.canvasSize,
			this.canvasSize
		)
	}
}

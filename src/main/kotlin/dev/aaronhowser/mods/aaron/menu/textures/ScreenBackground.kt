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
	fun extract(guiGraphics: GuiGraphicsExtractor, leftPos: Int, topPos: Int) {
		guiGraphics.blit(
			RenderPipelines.GUI_TEXTURED,
			texture,
			leftPos,
			topPos,
			0f,
			0f,
			width,
			height,
			canvasSize,
			canvasSize
		)
	}
}
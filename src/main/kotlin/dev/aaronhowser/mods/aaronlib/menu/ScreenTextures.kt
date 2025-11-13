package dev.aaronhowser.mods.aaronlib.menu

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

object ScreenTextures {

	open class Background(
		private val texture: ResourceLocation,
		val width: Int,
		val height: Int,
		private val canvasSize: Int = 256
	) {
//		private val texture = OtherUtil.modResource(path)

		fun render(guiGraphics: GuiGraphics, leftPos: Int, topPos: Int) {
			guiGraphics.blit(
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

	open class Sprite(
		val texture: ResourceLocation,
		val width: Int,
		val height: Int
	) {
//		val texture = OtherUtil.modResource(path)
	}

	//TODO: Check that all of these are actually used

}
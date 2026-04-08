package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

open class ItemStackButton(
	x: Int = 0,
	y: Int = 0,
	width: Int,
	height: Int,
	val itemStack: ItemStack,
	onPress: OnPress,
	message: Component = Component.empty(),
	private val font: Font
) : Button(x, y, width, height, message, onPress, DEFAULT_NARRATION) {

	override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
		extractDefaultSprite(graphics)
		renderItemStack(graphics)

		if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
			setToolTip(graphics, mouseX, mouseY)
		}
	}

	private fun setToolTip(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		if (this.message == Component.empty()) return

		guiGraphics.setComponentTooltipForNextFrame(
			font,
			listOf(this.message),
			mouseX,
			mouseY
		)
	}

	private fun renderItemStack(guiGraphics: GuiGraphicsExtractor) {
		if (itemStack.isEmpty) return

		guiGraphics.item(
			itemStack,
			x + (width - 16) / 2,
			y + (height - 16) / 2
		)
	}

}
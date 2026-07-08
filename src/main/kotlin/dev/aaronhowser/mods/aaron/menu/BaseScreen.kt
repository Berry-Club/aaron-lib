package dev.aaronhowser.mods.aaron.menu

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class BaseScreen<M : AbstractContainerMenu>(
	menu: M,
	playerInventory: Inventory,
	title: Component,
	protected val background: ScreenBackground
) : AbstractContainerScreen<M>(menu, playerInventory, title, background.width, background.height) {

	override fun isPauseScreen(): Boolean = false
	protected open val showTitleLabel = true
	protected open val showInventoryLabel = true

	protected val rightPos: Int
		get() = leftPos + imageWidth
	protected val bottomPos: Int
		get() = topPos + imageHeight

	open val titleLabelOffsetX: Int = 0
	open val titleLabelOffsetY: Int = 0

	open val inventoryLabelOffsetX: Int = 0
	open val inventoryLabelOffsetY: Int = 0

	final override fun init() {
		super.init()

		titleLabelX = 8
		titleLabelY = 6
		inventoryLabelX = 8
		inventoryLabelY = imageHeight - 94

		baseInit()
	}

	open fun baseInit() {}

	override fun extractLabels(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		if (showTitleLabel) {
			graphics.text(
				font,
				title,
				titleLabelX + titleLabelOffsetX,
				titleLabelY + titleLabelOffsetY,
				0xFF404040.toInt(),
				false
			)
		}

		if (showInventoryLabel) {
			graphics.text(
				font,
				playerInventoryTitle,
				inventoryLabelX + inventoryLabelOffsetX,
				inventoryLabelY + inventoryLabelOffsetY,
				0xFF404040.toInt(),
				false
			)
		}
	}

	override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick)

		this.background.render(
			graphics = graphics,
			leftPos = this.leftPos,
			topPos = this.topPos
		)
	}
}

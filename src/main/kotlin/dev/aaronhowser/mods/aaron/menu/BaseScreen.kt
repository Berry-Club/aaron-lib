package dev.aaronhowser.mods.aaron.menu

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class BaseScreen<M : AbstractContainerMenu>(
	menu: M,
	playerInventory: Inventory,
	title: Component
) : AbstractContainerScreen<M>(menu, playerInventory, title) {

	protected abstract val background: ScreenBackground

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
		imageWidth = background.width
		imageHeight = background.height

		leftPos = (width - imageWidth) / 2
		topPos = (height - imageHeight) / 2

		baseInit()
	}

	open fun baseInit() {}

	override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		super.render(guiGraphics, mouseX, mouseY, partialTick)
		this.renderTooltip(guiGraphics, mouseX, mouseY)
	}

	override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
		this.background.render(
			guiGraphics = guiGraphics,
			leftPos = this.leftPos,
			topPos = this.topPos
		)
	}

	override fun extractLabels(graphics: GuiGraphicsExtractor, xm: Int, ym: Int) {
		if (showTitleLabel) {
			graphics.text(
				font,
				title,
				titleLabelX + titleLabelOffsetX,
				titleLabelY + titleLabelOffsetY,
				-12566464,
				false
			)
		}

		if (showInventoryLabel) {
			graphics.text(
				font,
				playerInventoryTitle,
				inventoryLabelX + inventoryLabelOffsetX,
				inventoryLabelY + inventoryLabelOffsetY,
				-12566464,
				false
			)
		}
	}
}
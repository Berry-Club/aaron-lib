package dev.aaronhowser.mods.aaron.menu

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

abstract class MenuWithInventory(
	menuType: MenuType<*>?,
	containerId: Int,
	protected val playerInventory: Inventory,
) : AbstractContainerMenu(menuType, containerId) {

	protected open fun addPlayerInventorySlots(playerInventoryY: Int) {
		// Add the 27 slots of the player inventory
		for (row in 0 until 3) {
			val y = playerInventoryY + row * 18

			for (column in 0 until 9) {
				val slotIndex = column + row * 9 + 9
				val x = 8 + column * 18

				this.addSlot(Slot(playerInventory, slotIndex, x, y))
			}
		}

		val playerHotbarY = playerInventoryY + 58

		// Add the 9 slots of the player hotbar
		for (hotbarIndex in 0..8) {
			val x = 8 + hotbarIndex * 18

			this.addSlot(Slot(playerInventory, hotbarIndex, x, playerHotbarY))
		}
	}

	open fun addSlots() {}

	override fun quickMoveStack(player: Player, clickedSlotIndex: Int): ItemStack {
		val clickedSlot = slots.getOrNull(clickedSlotIndex)
		if (clickedSlot == null || !clickedSlot.hasItem()) return ItemStack.EMPTY

		val clickedStack = clickedSlot.item
		val originalStack = clickedStack.copy()

		val totalSlotCount = slots.size
		val playerInventoryStartIndex = totalSlotCount - 36
		val playerInventoryEndIndex = totalSlotCount - 1
		val machineSlotEndIndex = playerInventoryStartIndex - 1

		val clickedSlotIsInMachine = clickedSlotIndex <= machineSlotEndIndex
		val clickedSlotIsInPlayerInventory = clickedSlotIndex in playerInventoryStartIndex..playerInventoryEndIndex

		val wasItemMoved = when {
			clickedSlotIsInMachine -> moveItemStackTo(clickedStack, playerInventoryStartIndex, totalSlotCount, true)
			clickedSlotIsInPlayerInventory -> moveItemStackTo(clickedStack, 0, playerInventoryStartIndex, false)
			else -> false
		}

		if (!wasItemMoved) return ItemStack.EMPTY

		if (clickedStack.isEmpty) {
			clickedSlot.setByPlayer(ItemStack.EMPTY)
		} else {
			clickedSlot.setChanged()
		}

		val stackCountChanged = clickedStack.count != originalStack.count
		if (!stackCountChanged) return ItemStack.EMPTY

		clickedSlot.onTake(player, clickedStack)
		return originalStack
	}

}
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

	/**
	 * Adds the player inventory slots first, then calls [addContainerSlots].
	 */
	protected open fun addSlots(playerInventoryY: Int) {
		require(slots.isEmpty()) {
			"MenuWithInventory#addSlots must be called before adding any slots"
		}

		addPlayerInventorySlots(playerInventoryY)
		addContainerSlots()
		requirePlayerInventorySlotsFirst()
	}

	protected open fun addContainerSlots() {}

	override fun quickMoveStack(player: Player, clickedSlotIndex: Int): ItemStack {
		val clickedSlot = slots.getOrNull(clickedSlotIndex)
		if (clickedSlot == null || !clickedSlot.hasItem()) return ItemStack.EMPTY

		val clickedStack = clickedSlot.item
		val originalStack = clickedStack.copy()

		val wasItemMoved = if (clickedSlotIndex < PLAYER_INVENTORY_SLOT_COUNT) {
			moveFromPlayerInventory(clickedStack, clickedSlotIndex)
		} else {
			moveItemStackTo(clickedStack, 0, PLAYER_INVENTORY_SLOT_COUNT, true)
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

	private fun moveFromPlayerInventory(stack: ItemStack, clickedSlotIndex: Int): Boolean {
		if (moveItemStackTo(stack, MACHINE_SLOT_START_INDEX, slots.size, false)) return true

		return if (clickedSlotIndex < HOTBAR_START_INDEX) {
			moveItemStackTo(stack, HOTBAR_START_INDEX, PLAYER_INVENTORY_SLOT_COUNT, false)
		} else {
			moveItemStackTo(stack, 0, HOTBAR_START_INDEX, false)
		}
	}

	private fun requirePlayerInventorySlotsFirst() {
		require(slots.size >= PLAYER_INVENTORY_SLOT_COUNT) {
			"MenuWithInventory requires the first $PLAYER_INVENTORY_SLOT_COUNT slots to be the player inventory, but only ${slots.size} slots were added"
		}

		for (slotIndex in 0 until PLAYER_INVENTORY_SLOT_COUNT) {
			require(slots[slotIndex].container === playerInventory) {
				"MenuWithInventory requires player inventory slots to be added before machine slots; slot $slotIndex belongs to ${slots[slotIndex].container}"
			}
		}
	}

	private companion object {
		private const val PLAYER_INVENTORY_SLOT_COUNT = 36
		private const val HOTBAR_START_INDEX = 27
		private const val MACHINE_SLOT_START_INDEX = PLAYER_INVENTORY_SLOT_COUNT
	}

}

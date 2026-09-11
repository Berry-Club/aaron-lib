package dev.aaronhowser.mods.aaron.menu

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

abstract class HeldItemMenu(
	menuType: MenuType<*>?,
	containerId: Int,
	playerInventory: Inventory,
	protected val usedHand: InteractionHand,
) : MenuWithInventory(menuType, containerId, playerInventory) {

	private val lockedInventorySlot = when (usedHand) {
		InteractionHand.MAIN_HAND -> playerInventory.selected
		InteractionHand.OFF_HAND -> Inventory.SLOT_OFFHAND
	}

	private var lockedMenuSlot = NO_SLOT

	final override fun addPlayerSlot(inventorySlot: Int, x: Int, y: Int) {
		val slot = if (inventorySlot == lockedInventorySlot) {
			UnmodifiableSlot(playerInventory, inventorySlot, x, y)
		} else {
			Slot(playerInventory, inventorySlot, x, y)
		}

		addSlot(slot)
		if (inventorySlot == lockedInventorySlot) {
			lockedMenuSlot = slots.lastIndex
		}
	}

	override fun clicked(slotId: Int, button: Int, clickType: ClickType, player: Player) {
		if (slotId == lockedMenuSlot) return
		if (clickType == ClickType.SWAP && button == lockedInventorySlot) return

		super.clicked(slotId, button, clickType, player)
	}

	override fun quickMoveStack(player: Player, clickedSlotIndex: Int): ItemStack {
		if (clickedSlotIndex == lockedMenuSlot) return ItemStack.EMPTY

		return super.quickMoveStack(player, clickedSlotIndex)
	}

	final override fun stillValid(player: Player): Boolean {
		return isValidHeldItem(player.getItemInHand(usedHand))
	}

	protected abstract fun isValidHeldItem(heldItem: ItemStack): Boolean

	companion object {
		private const val NO_SLOT = -1
	}

}
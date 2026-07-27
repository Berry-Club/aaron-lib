package dev.aaronhowser.mods.aaron.menu

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

abstract class HeldItemMenuWithoutInventory(
	menuType: MenuType<*>?,
	containerId: Int,
	protected val usedHand: InteractionHand,
) : AbstractContainerMenu(menuType, containerId) {

	final override fun stillValid(player: Player): Boolean {
		return isValidHeldItem(player.getItemInHand(usedHand))
	}

	protected abstract fun isValidHeldItem(heldItem: ItemStack): Boolean

}
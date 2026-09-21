package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ContainerSlot(
	container: Container,
	private val slotIndex: Int,
	x: Int,
	y: Int
) : Slot(container, slotIndex, x, y) {

	override fun mayPlace(stack: ItemStack): Boolean {
		return container.canPlaceItem(slotIndex, stack)
	}

}
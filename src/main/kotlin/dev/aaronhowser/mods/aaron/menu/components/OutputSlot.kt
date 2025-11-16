package dev.aaronhowser.mods.aaron.menu.components

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class OutputSlot(container: Container, slot: Int, x: Int, y: Int) : Slot(container, slot, x, y) {

	override fun mayPlace(stack: ItemStack): Boolean = false

}
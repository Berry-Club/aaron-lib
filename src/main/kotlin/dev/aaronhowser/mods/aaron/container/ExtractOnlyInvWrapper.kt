package dev.aaronhowser.mods.aaron.container

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.wrapper.InvWrapper

open class ExtractOnlyInvWrapper(inv: Container) : InvWrapper(inv) {
	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = stack
}
package dev.aaronhowser.mods.aaron.misc.extensions

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Unit
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

object AaronItemExtensions {

	fun ItemLike.getDefaultInstance(): ItemStack = this.asItem().defaultInstance

	fun <T : Any> ItemLike.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.set(componentType, component)
		return stack
	}

	fun <T : Any> ItemStack.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		this.set(componentType, component)
		return this
	}

	fun ItemLike.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.remove(componentType)
		return stack
	}

	fun ItemStack.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		this.remove(componentType)
		return this
	}

	fun ItemLike.withCount(count: Int): ItemStack {
		val stack = getDefaultInstance()
		stack.count = count
		return stack
	}

	fun List<ItemStack>.totalCount(): Int {
		var total = 0
		for (stack in this) {
			total += stack.count
		}
		return total
	}

	fun Holder<Potion>.getAsStack(): ItemStack {
		return PotionContents.createItemStack(Items.POTION, this)
	}

	fun ItemStack.hasEnchantment(enchantment: Holder<Enchantment>): Boolean = this.getEnchantmentLevel(enchantment) > 0
	fun ItemStack.isNotEmpty(): Boolean = !this.isEmpty
	fun ItemStack.isFull(): Boolean = this.count >= this.maxStackSize
	fun ItemStack.isNotFull(): Boolean = this.count < this.maxStackSize

	fun ItemStack.setUnit(dataComponent: DataComponentType<Unit>) = this.set(dataComponent, Unit.INSTANCE)
	fun ItemStack.setUnit(dataComponent: Supplier<out DataComponentType<Unit>>) = setUnit(dataComponent.get())

}
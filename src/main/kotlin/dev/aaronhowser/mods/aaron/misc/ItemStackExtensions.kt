package dev.aaronhowser.mods.aaron.misc

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.util.Unit
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import java.util.function.Predicate
import java.util.function.Supplier

@Suppress("unused")
object ItemStackExtensions {
	fun ItemStack.isItem(item: Holder<Item>): Boolean = this.`is`(item)
	fun ItemStack.isItem(item: Item): Boolean = this.`is`(item)
	fun ItemStack.isItem(tag: TagKey<Item>): Boolean = this.`is`(tag)

	fun ItemLike.asIngredient(): Ingredient = Ingredient.of(this)
	fun TagKey<Item>.asIngredient(): Ingredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(this))
	fun ItemStack.asIngredient(strict: Boolean = false): Ingredient {
		return if (isComponentsPatchEmpty) {
			Ingredient.of(this.item)
		} else {
			DataComponentIngredient.of(strict, this)
		}
	}

	fun <T : Any> ItemLike.asIngredient(
		componentType: DataComponentType<in T>,
		component: T,
		strict: Boolean = false
	): Ingredient {
		return DataComponentIngredient.of(strict, componentType, component, this)
	}

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

	fun Player.giveOrDropStack(itemStack: ItemStack): Boolean {
		if (this.inventory.add(itemStack)) return true

		val entity = ItemEntity(level(), this.x, this.y, this.z, itemStack)
		entity.setNoPickUpDelay()
		return this.level().addFreshEntity(entity)
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

	fun ItemStack.toggleUnit(dataComponent: DataComponentType<Unit>) {
		if (this.has(dataComponent)) {
			this.remove(dataComponent)
		} else {
			this.setUnit(dataComponent)
		}
	}

	fun ItemStack.toggleUnit(dataComponent: Supplier<out DataComponentType<Unit>>) = toggleUnit(dataComponent.get())

	fun Player.allItemStacks(): List<ItemStack> = inventory.nonEquipmentItems
	fun Player.allItemStacksSequence(): Sequence<ItemStack> = inventory.nonEquipmentItems.asSequence()
	fun Player.getFirstItemStack(predicate: Predicate<ItemStack>): ItemStack? = allItemStacksSequence().firstOrNull(predicate::test)
}

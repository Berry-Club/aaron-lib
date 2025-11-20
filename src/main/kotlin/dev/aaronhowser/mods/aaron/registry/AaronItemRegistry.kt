package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

abstract class AaronItemRegistry {

	abstract fun getItemRegistry(): DeferredRegister.Items

	protected fun basic(id: String): DeferredItem<Item> {
		return basic(id) { Item.Properties() }
	}

	protected fun basic(id: String, properties: Item.Properties): DeferredItem<Item> {
		return basic(id) { properties }
	}

	protected fun basic(id: String, properties: () -> Item.Properties): DeferredItem<Item> {
		return getItemRegistry().registerItem(id) { Item(properties()) }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: () -> Item.Properties = { Item.Properties() }
	): DeferredItem<I> {
		return getItemRegistry().registerItem(id) { builder(properties()) }
	}

	protected fun registerItemNameBlockItem(
		id: String,
		block: DeferredBlock<out Block>,
		properties: Item.Properties = Item.Properties()
	): DeferredItem<ItemNameBlockItem> {
		return getItemRegistry().registerItem(id) { ItemNameBlockItem(block.get(), properties) }
	}

}
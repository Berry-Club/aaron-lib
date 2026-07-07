package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

abstract class AaronItemRegistry {

	abstract fun getItemRegistry(): DeferredRegister.Items

	protected fun basic(id: String): DeferredItem<Item> {
		return basic(id) { Item.Properties() }
	}

	protected fun basic(id: String, properties: Item.Properties): DeferredItem<Item> {
		return basic(id) { properties }
	}

	protected fun basic(id: String, properties: () -> Item.Properties): DeferredItem<Item> {
		return register(id, ::Item, properties)
	}

	protected fun basic(id: String, properties: Supplier<Item.Properties>): DeferredItem<Item> {
		return basic(id) { properties.get() }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: () -> Item.Properties = { Item.Properties() }
	): DeferredItem<I> {
		return getItemRegistry().registerItem(
			id,
			Function { itemProperties -> builder(itemProperties) },
			Supplier { properties() }
		)
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: Item.Properties
	): DeferredItem<I> {
		return register(id, builder) { properties }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: Supplier<Item.Properties>
	): DeferredItem<I> {
		return register(id, builder) { properties.get() }
	}

	protected fun registerItemNameBlockItem(
		id: String,
		block: DeferredBlock<out Block>,
		properties: Item.Properties = Item.Properties()
	): DeferredItem<Item> {
		return register(id, ::Item) { properties.useItemDescriptionPrefix() }
	}

	protected fun registerSpawnEgg(
		name: String,
		entityType: () -> EntityType<out Mob>,
		properties: () -> Item.Properties = { Item.Properties() }
	): DeferredItem<SpawnEggItem> {
		return register(
			name, { itemProperties ->
				SpawnEggItem(itemProperties.spawnEgg(entityType()))
			},
			properties
		)
	}

	companion object {
		val PROPERTIES_SINGLE_STACK: Item.Properties = Item.Properties().stacksTo(1)
	}

}

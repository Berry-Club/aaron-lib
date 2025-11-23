package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.ForgeSpawnEggItem
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

abstract class AaronItemRegistry {

	abstract fun getItemRegistry(): DeferredRegister<Item>

	protected fun basic(id: String): RegistryObject<Item> {
		return basic(id) { Item.Properties() }
	}

	protected fun basic(id: String, properties: Item.Properties): RegistryObject<Item> {
		return basic(id) { properties }
	}

	protected fun basic(id: String, properties: () -> Item.Properties): RegistryObject<Item> {
		return getItemRegistry().register(id) { Item(properties()) }
	}

	protected fun basic(id: String, properties: Supplier<Item.Properties>): RegistryObject<Item> {
		return getItemRegistry().register(id) { Item(properties.get()) }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: () -> Item.Properties = { Item.Properties() }
	): RegistryObject<I> {
		return getItemRegistry().register(id) { builder(properties()) }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: Item.Properties
	): RegistryObject<I> {
		return getItemRegistry().register(id) { builder(properties) }
	}

	protected fun <I : Item> register(
		id: String,
		builder: (Item.Properties) -> I,
		properties: Supplier<Item.Properties>
	): RegistryObject<I> {
		return getItemRegistry().register(id) { builder(properties.get()) }
	}

	protected fun registerItemNameBlockItem(
		id: String,
		block: RegistryObject<out Block>,
		properties: Item.Properties = Item.Properties()
	): RegistryObject<ItemNameBlockItem> {
		return getItemRegistry().register(id) { ItemNameBlockItem(block.get(), properties) }
	}

	protected fun registerSpawnEgg(
		name: String,
		entityType: () -> EntityType<out Mob>,
		backgroundColor: Int,
		highlightColor: Int,
		properties: () -> Item.Properties = { Item.Properties() }
	): RegistryObject<SpawnEggItem> {
		return getItemRegistry()
			.register(
				name
			) {
				ForgeSpawnEggItem(
					entityType,
					backgroundColor,
					highlightColor,
					properties()
				)
			}
	}

}
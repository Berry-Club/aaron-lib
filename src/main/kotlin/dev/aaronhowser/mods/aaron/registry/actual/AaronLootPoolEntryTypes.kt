package dev.aaronhowser.mods.aaron.registry.actual

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.datagen.LootItemStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object AaronLootPoolEntryTypes {

	val LOOT_POOL_ENTRY_TYPE_REGISTRY: DeferredRegister<MapCodec<out LootPoolEntryContainer>> =
		DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, AaronLib.MOD_ID)

	val ITEM_STACK: DeferredHolder<MapCodec<out LootPoolEntryContainer>, MapCodec<LootItemStack>> =
		register("item_stack", LootItemStack.CODEC)

	private fun <T : LootPoolEntryContainer> register(
		name: String,
		codec: MapCodec<T>
	): DeferredHolder<MapCodec<out LootPoolEntryContainer>, MapCodec<T>> {
		return LOOT_POOL_ENTRY_TYPE_REGISTRY.register(name, Supplier { codec })
	}

}

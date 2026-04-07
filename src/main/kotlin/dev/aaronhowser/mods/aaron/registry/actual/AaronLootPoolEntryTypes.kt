package dev.aaronhowser.mods.aaron.registry.actual

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.datagen.LootItemStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object AaronLootPoolEntryTypes {

//	val LOOT_POOL_ENTRY_TYPE_REGISTRY: DeferredRegister<LootPoolEntryType> =
//		DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, AaronLib.MOD_ID)
//
//	val ITEM_STACK: DeferredHolder<LootPoolEntryType, LootPoolEntryType> =
//		register("item_stack", LootItemStack.CODEC)
//
//	private fun register(
//		name: String,
//		codec: MapCodec<out LootPoolEntryContainer>
//	): DeferredHolder<LootPoolEntryType, LootPoolEntryType> {
//		return LOOT_POOL_ENTRY_TYPE_REGISTRY.register(name, Supplier { LootPoolEntryType(codec) })
//	}

}
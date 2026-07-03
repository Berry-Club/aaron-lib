package dev.aaronhowser.mods.aaron.datagen

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.registry.actual.AaronLootPoolEntryTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.function.Consumer

class LootItemStack(
	val itemStack: ItemStack,
	weight: Int,
	quality: Int,
	conditions: MutableList<LootItemCondition>,
	functions: MutableList<LootItemFunction>
) : LootPoolSingletonContainer(weight, quality, conditions, functions) {

	override fun createItemStack(stackConsumer: Consumer<ItemStack>, lootContext: LootContext) {
		stackConsumer.accept(itemStack.copy())
	}

	override fun codec(): MapCodec<out LootPoolSingletonContainer> {
		return AaronLootPoolEntryTypes.ITEM_STACK.get()
	}

	companion object {
		val CODEC: MapCodec<LootItemStack> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					ItemStack.CODEC
						.fieldOf("item_stack")
						.forGetter(LootItemStack::itemStack)
				)
					.and(singletonFields(instance))
					.apply(instance, ::LootItemStack)
			}

		fun lootTableStack(itemStack: ItemStack): Builder<*> {
			return simpleBuilder { weight, quality, conditions, functions ->
				LootItemStack(itemStack, weight, quality, conditions, functions)
			}
		}
	}

}

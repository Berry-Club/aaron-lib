package dev.aaronhowser.mods.aaron.datagen.loot

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.registry.actual.AaronLootPoolEntryTypes
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.entries.SingleEntryContainerBase
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.Optional
import java.util.function.Consumer

class LootItemStack(
	val itemStack: ItemStack,
	weight: Int,
	quality: Int,
	condition: Optional<Holder<LootItemCondition>>,
	modifier: Optional<Holder<LootItemFunction>>
) : SingleEntryContainerBase(weight, quality, condition, modifier) {

	override fun createItemStack(stackConsumer: Consumer<ItemStack>, lootContext: LootContext) {
		stackConsumer.accept(itemStack.copy())
	}

	override fun codec(): MapCodec<out SingleEntryContainerBase> {
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
					.and(uniformFields(instance))
					.apply(instance, ::LootItemStack)
			}

		fun lootTableStack(itemStack: ItemStack): UniformContainerBase.Builder<*> {
			return simpleBuilder { weight, quality, conditions, functions ->
				LootItemStack(itemStack, weight, quality, conditions, functions)
			}
		}
	}

}
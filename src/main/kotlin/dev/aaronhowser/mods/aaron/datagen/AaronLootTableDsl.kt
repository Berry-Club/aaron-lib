package dev.aaronhowser.mods.aaron.datagen

import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

@DslMarker
private annotation class LootTableDslMarker

object AaronLootTableDsl {

	fun table(configure: TableBuilder.() -> Unit): LootTable.Builder {
		return TableBuilder().apply(configure).build()
	}

	fun pool(configure: PoolBuilder.() -> Unit): LootPool.Builder {
		return PoolBuilder().apply(configure).build()
	}

	fun singleItemChancePool(
		item: ItemLike,
		chance: Float,
		denominator: Int = 10_000
	): LootPool.Builder {
		val itemWeight = Mth.ceil(chance * denominator)
		val emptyWeight = denominator - itemWeight

		return pool {
			empty(emptyWeight)
			item(item, itemWeight)
		}
	}

	@LootTableDslMarker
	class TableBuilder internal constructor() {
		private val builder = LootTable.lootTable()

		fun pool(pool: LootPool.Builder) {
			builder.withPool(pool)
		}

		fun pool(configure: PoolBuilder.() -> Unit) {
			pool(AaronLootTableDsl.pool(configure))
		}

		internal fun build(): LootTable.Builder = builder
	}

	@LootTableDslMarker
	class PoolBuilder internal constructor() {
		private val builder = LootPool.lootPool()

		fun rolls(amount: Float) {
			builder.setRolls(ConstantValue.exactly(amount))
		}

		fun item(
			item: ItemLike,
			weight: Int? = null,
			configure: LootPoolSingletonContainer.Builder<*>.() -> Unit = {}
		) {
			val entry = LootItem.lootTableItem(item)
			configure(entry)
			if (weight != null) entry.setWeight(weight)
			builder.add(entry)
		}

		fun stack(
			stack: ItemStack,
			weight: Int? = null,
			configure: LootPoolSingletonContainer.Builder<*>.() -> Unit = {}
		) {
			val entry = LootItemStack.lootTableStack(stack)
			configure(entry)
			if (weight != null) entry.setWeight(weight)
			builder.add(entry)
		}

		fun empty(weight: Int? = null) {
			val entry = EmptyLootItem.emptyItem()
			if (weight != null) entry.setWeight(weight)
			builder.add(entry)
		}

		fun condition(condition: LootItemCondition.Builder) {
			builder.`when`(condition)
		}

		internal fun build(): LootPool.Builder = builder
	}

}

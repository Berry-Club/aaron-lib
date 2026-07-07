package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

abstract class AaronBlockRegistry {

	abstract fun getBlockRegistry(): DeferredRegister.Blocks

	protected abstract fun getItemRegistry(): DeferredRegister.Items

	protected fun blockWithProperties(name: String, properties: Properties): DeferredBlock<Block> =
		registerBlock(name, ::Block, properties = { properties })

	protected fun basicBlock(name: String): DeferredBlock<Block> =
		blockWithProperties(name, Properties.of())

	protected fun basicGlassBlock(name: String) =
		registerBlock(name, ::TransparentBlock, properties = { Properties.ofFullCopy(Blocks.GLASS) })

	protected fun basicCopiedBlock(name: String, blockToCopy: Block) =
		blockWithProperties(name, Properties.ofFullCopy(blockToCopy))

	protected fun basicStoneBlock(name: String) =
		basicCopiedBlock(name, Blocks.STONE)

	protected fun <T : Block> registerBlock(
		name: String,
		factory: (Properties) -> T,
		properties: () -> Properties = { Properties.of() },
		itemProperties: () -> Item.Properties = { Item.Properties() }
	): DeferredBlock<T> {
		val block = registerBlockWithoutItem(name, factory, properties)
		registerBlockItem(block, itemProperties)
		return block
	}

	protected fun <T : Block> registerBlockWithoutItem(
		name: String,
		factory: (Properties) -> T,
		properties: () -> Properties = { Properties.of() }
	): DeferredBlock<T> {
		return getBlockRegistry().registerBlock(
			name,
			Function { blockProperties -> factory(blockProperties) },
			Supplier { properties() }
		)
	}

	protected fun registerBlockItem(
		block: DeferredBlock<out Block>,
		properties: () -> Item.Properties = { Item.Properties() }
	): DeferredItem<BlockItem> {
		return getItemRegistry().registerSimpleBlockItem(block, Supplier { properties() })
	}

}

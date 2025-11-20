package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

abstract class AaronBlockRegistry {

	abstract fun getBlockRegistry(): DeferredRegister.Blocks

	protected abstract fun getItemRegistry(): DeferredRegister.Items

	protected fun blockWithProperties(name: String, properties: Properties): DeferredBlock<Block> =
		registerBlock(name) { Block(properties) }

	protected fun basicBlock(name: String): DeferredBlock<Block> =
		blockWithProperties(name, Properties.of())

	protected fun basicGlassBlock(name: String) =
		registerBlock(name) { TransparentBlock(Properties.ofFullCopy(Blocks.GLASS)) }

	protected fun basicCopiedBlock(name: String, blockToCopy: Block) =
		blockWithProperties(name, Properties.ofFullCopy(blockToCopy))

	protected fun basicStoneBlock(name: String) =
		basicCopiedBlock(name, Blocks.STONE)

	protected  fun <T : Block> registerBlock(
		name: String,
		supplier: () -> T
	): DeferredBlock<T> {
		val block = getBlockRegistry().register(name, supplier)
		getItemRegistry().registerSimpleBlockItem(name, block)
		return block
	}

	protected fun <T : Block> registerBlockWithoutItem(
		name: String,
		supplier: () -> T
	): DeferredBlock<T> {
		return getBlockRegistry().register(name, supplier)
	}

}
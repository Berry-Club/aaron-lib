package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.GlassBlock
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

abstract class AaronBlockRegistry {

	abstract fun getBlockRegistry(): DeferredRegister<Block>
	protected abstract fun getItemRegistry(): DeferredRegister<Item>

	protected fun blockWithProperties(name: String, properties: Properties): RegistryObject<Block> =
		registerBlock(name) { Block(properties) }

	protected fun basicBlock(name: String): RegistryObject<Block> =
		blockWithProperties(name, Properties.of())

	protected fun basicGlassBlock(name: String) =
		registerBlock(name) { GlassBlock(Properties.copy(Blocks.GLASS)) }

	protected fun basicCopiedBlock(name: String, blockToCopy: Block) =
		blockWithProperties(name, Properties.copy(blockToCopy))

	protected fun basicStoneBlock(name: String) =
		basicCopiedBlock(name, Blocks.STONE)

	protected fun <T : Block> registerBlock(
		name: String,
		supplier: () -> T
	): RegistryObject<T> {
		val block = getBlockRegistry().register(name, supplier)
		getItemRegistry().register(name) { BlockItem(block.get(), Item.Properties()) }
		return block
	}

	protected fun <T : Block> registerBlockWithoutItem(
		name: String,
		supplier: () -> T
	): RegistryObject<T> {
		return getBlockRegistry().register(name, supplier)
	}

}
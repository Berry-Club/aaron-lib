package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronBlockEntityTypeRegistry {

	abstract fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>>

	@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
	protected fun <T : BlockEntity> register(
		name: String,
		builder: BlockEntityType.BlockEntitySupplier<out T>,
		vararg validBlocks: DeferredBlock<*>
	): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
		return getBlockEntityRegistry().register(name, Supplier {
			BlockEntityType.Builder.of(
				builder,
				*validBlocks.map(DeferredBlock<*>::get).toTypedArray()
			).build(null)
		})
	}

}
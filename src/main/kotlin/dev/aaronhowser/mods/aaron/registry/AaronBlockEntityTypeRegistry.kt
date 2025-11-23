package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

abstract class AaronBlockEntityTypeRegistry {

	abstract fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>>

	@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
	protected fun <T : BlockEntity> register(
		name: String,
		builder: BlockEntityType.BlockEntitySupplier<out T>,
		vararg validBlocks: RegistryObject<out Block>
	): RegistryObject<BlockEntityType<T>> {
		return getBlockEntityRegistry().register(name, Supplier {
			BlockEntityType.Builder.of(
				builder,
				*validBlocks.map(RegistryObject<out Block>::get).toTypedArray()
			).build(null)
		})
	}

}
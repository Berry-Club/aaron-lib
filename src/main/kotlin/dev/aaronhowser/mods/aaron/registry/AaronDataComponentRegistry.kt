package dev.aaronhowser.mods.aaron.registry

import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Unit
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

abstract class AaronDataComponentRegistry {

	abstract fun getDataComponentRegistry(): DeferredRegister.DataComponents

	protected fun unit(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> {
		return register(name, Unit.CODEC, StreamCodec.unit(Unit.INSTANCE))
	}

	protected fun <T> register(
		name: String,
		codec: Codec<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
		return getDataComponentRegistry().registerComponentType(name) {
			it.persistent(codec).networkSynchronized(streamCodec)
		}
	}

}
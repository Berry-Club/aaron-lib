package dev.aaronhowser.mods.aaron.registry

import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Unit
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

abstract class AaronDataComponentRegistry {

	abstract fun getDataComponentRegistry(): DeferredRegister.DataComponents

	protected fun unit(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> {
		return register(name, Unit.CODEC, StreamCodec.unit(Unit.INSTANCE))
	}

	protected fun boolean(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> {
		return register(name, Codec.BOOL, ByteBufCodecs.BOOL)
	}

	protected fun int(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Int>> {
		return register(name, Codec.INT, ByteBufCodecs.INT)
	}

	protected fun float(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Float>> {
		return register(name, Codec.FLOAT, ByteBufCodecs.FLOAT)
	}

	protected fun double(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Double>> {
		return register(name, Codec.DOUBLE, ByteBufCodecs.DOUBLE)
	}

	protected fun long(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Long>> {
		return register(name, Codec.LONG, ByteBufCodecs.VAR_LONG)
	}

	protected fun string(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<String>> {
		return register(name, Codec.STRING, ByteBufCodecs.STRING_UTF8)
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
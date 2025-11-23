package dev.aaronhowser.mods.aaron.data_component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

abstract class PseudoDataComponent {

	abstract class Type(val id: ResourceLocation) {
		abstract fun <T : PseudoDataComponent> getCodec(): Codec<T>
	}

	abstract val type: Type

	companion object {
		fun ItemStack.hasComponent(type: Type): Boolean {
			return this.tag?.contains(type.id.toString()).isTrue()
		}

		fun <T : PseudoDataComponent> ItemStack.saveComponent(component: T) {
			val codec = component.type.getCodec<T>()
			val encoded = codec.encodeStart(NbtOps.INSTANCE, component).getOrThrow(false) {}

			this.getOrCreateTag().put(component.type.id.toString(), encoded)
		}

		fun <T : PseudoDataComponent> ItemStack.loadComponent(id: ResourceLocation, type: Type): T? {
			val tag = this.tag ?: return null
			val nbt = tag.get(id.toString()) ?: return null

			val pair = type
				.getCodec<T>()
				.decode(NbtOps.INSTANCE, nbt)
				.getOrThrow(false) {}

			return pair.first
		}
	}
}
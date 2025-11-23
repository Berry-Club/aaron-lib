package dev.aaronhowser.mods.aaron.data_component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

abstract class PseudoDataComponent<
		C : PseudoDataComponent<C, T>,
		T : PseudoDataComponent.Type<C>
		> {

	abstract class Type<C : PseudoDataComponent<C, *>>(val id: ResourceLocation) {
		abstract fun getCodec(): Codec<C>
	}

	abstract val type: T

	companion object {
		fun ItemStack.hasComponent(type: Type<*>): Boolean {
			return this.tag?.contains(type.id.toString()).isTrue()
		}

		fun <C : PseudoDataComponent<C, *>> ItemStack.saveComponent(component: C) {
			val codec = component.type.getCodec()
			val encoded = codec.encodeStart(NbtOps.INSTANCE, component).getOrThrow(false) {}

			this.getOrCreateTag().put(component.type.id.toString(), encoded)
		}

		fun <C : PseudoDataComponent<C, T>, T : Type<C>> ItemStack.loadComponent(id: ResourceLocation, type: T): C? {
			val tag = this.tag ?: return null
			val nbt = tag.get(id.toString()) ?: return null

			val pair = type
				.getCodec()
				.decode(NbtOps.INSTANCE, nbt)
				.getOrThrow(false) {}

			return pair.first
		}
	}
}
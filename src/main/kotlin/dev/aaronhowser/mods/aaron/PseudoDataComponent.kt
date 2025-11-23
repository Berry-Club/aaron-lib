package dev.aaronhowser.mods.aaron

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

abstract class PseudoDataComponent(
	val id: ResourceLocation
) {

	abstract fun <T : PseudoDataComponent> getCodec(): Codec<T>

	companion object {
		fun ItemStack.hasComponent(id: ResourceLocation): Boolean {
			return this.tag?.contains(id.toString()).isTrue()
		}

		fun <T : PseudoDataComponent> ItemStack.saveComponent(component: T) {
			val codec = component.getCodec<T>()
			val encoded = codec.encodeStart(NbtOps.INSTANCE, component).getOrThrow(false) {}

			this.orCreateTag.put(component.id.toString(), encoded)
		}
	}

}
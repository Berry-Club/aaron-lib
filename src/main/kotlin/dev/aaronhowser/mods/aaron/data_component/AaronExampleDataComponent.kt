package dev.aaronhowser.mods.aaron.data_component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronLib

data class AaronExampleDataComponent(
	val exampleInt: Int
) : PseudoDataComponent() {

	class Type : PseudoDataComponent.Type(AaronLib.modResource("example")) {
		override fun <T : PseudoDataComponent> getCodec(): Codec<T> = CODEC

		companion object {
			val CODEC: Codec<AaronExampleDataComponent> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Codec.INT
							.fieldOf("example_int")
							.forGetter(AaronExampleDataComponent::exampleInt)
					).apply(instance, ::AaronExampleDataComponent)
				}

		}
	}

	override val type: PseudoDataComponent.Type = Type()

}
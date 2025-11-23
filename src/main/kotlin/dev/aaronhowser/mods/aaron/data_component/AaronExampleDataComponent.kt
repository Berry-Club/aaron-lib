package dev.aaronhowser.mods.aaron.data_component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronLib

data class AaronExampleDataComponent(
	val exampleInt: Int,
	val doubles: List<Double>
) : PseudoDataComponent<AaronExampleDataComponent, AaronExampleDataComponent.Type>() {

	class Type : PseudoDataComponent.Type<AaronExampleDataComponent>(AaronLib.modResource("example")) {
		override fun getCodec(): Codec<AaronExampleDataComponent> = CODEC

		companion object {
			val CODEC: Codec<AaronExampleDataComponent> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Codec.INT
							.fieldOf("example_int")
							.forGetter(AaronExampleDataComponent::exampleInt),
						Codec.DOUBLE.listOf()
							.fieldOf("doubles")
							.forGetter(AaronExampleDataComponent::doubles)
					).apply(instance, ::AaronExampleDataComponent)
				}

		}
	}

	override val type: Type = Type()

}
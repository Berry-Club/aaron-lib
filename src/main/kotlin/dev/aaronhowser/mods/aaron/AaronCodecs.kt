package dev.aaronhowser.mods.aaron

import com.mojang.serialization.Codec
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object AaronCodecs {

	val COMPONENT_CODEC: Codec<MutableComponent> =
		Codec.STRING.xmap(
			Component.Serializer::fromJson,
			Component.Serializer::toJson
		)

}
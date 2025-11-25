package dev.aaronhowser.mods.aaron

import com.mojang.serialization.Codec
import net.minecraft.network.chat.Component

object AaronCodecs {

	val COMPONENT_CODEC: Codec<Component> =
		Codec.STRING.xmap(
			Component.Serializer::fromJson,
			Component.Serializer::toJson
		)

}
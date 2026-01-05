package dev.aaronhowser.mods.aaron

import com.mojang.serialization.Codec
import net.minecraft.Util
import net.minecraft.network.chat.Component
import org.joml.Vector2d

object AaronExtraCodecs {

	val VECTOR2D_CODEC: Codec<Vector2d> =
		Codec.DOUBLE
			.listOf()
			.comapFlatMap(
				{ list -> Util.fixedSize(list, 2).map { Vector2d(it[0], it[1]) } },
				{ vector -> listOf(vector.x, vector.y) }
			)

	val UINT_CODEC: Codec<UInt> =
		Codec.INT.xmap(Int::toUInt, UInt::toInt)

	val COMPONENT_CODEC: Codec<Component> =
		Codec.STRING
			.xmap(
				{ str -> Component.Serializer.fromJson(str) },
				{ comp -> Component.Serializer.toJson(comp) }
			)

}
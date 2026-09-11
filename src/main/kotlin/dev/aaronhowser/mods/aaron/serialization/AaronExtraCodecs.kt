package dev.aaronhowser.mods.aaron.serialization

import com.mojang.serialization.Codec
import net.minecraft.Util
import org.joml.Vector2d

object AaronExtraCodecs {

	val VECTOR2D: Codec<Vector2d> =
		Codec.DOUBLE
			.listOf()
			.comapFlatMap(
				{ list -> Util.fixedSize(list, 2).map { Vector2d(it[0], it[1]) } },
				{ vector -> listOf(vector.x, vector.y) }
			)

	val UINT: Codec<UInt> =
		Codec.INT.xmap(Int::toUInt, UInt::toInt)

}
package dev.aaronhowser.mods.aaron.serialization

import com.mojang.serialization.Codec

object AaronExtraCodecs {

	val UINT: Codec<UInt> =
		Codec.INT.xmap(Int::toUInt, UInt::toInt)

}
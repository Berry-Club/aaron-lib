package dev.aaronhowser.mods.aaron.serialization

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.Util
import net.minecraft.core.NonNullList
import net.minecraft.core.Registry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
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

}
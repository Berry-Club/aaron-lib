package dev.aaronhowser.mods.aaron

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
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
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d

object AaronExtraCodecs {

	val VEC3_STREAM_CODEC: StreamCodec<ByteBuf, Vec3> =
		object : StreamCodec<ByteBuf, Vec3> {
			override fun decode(buffer: ByteBuf): Vec3 = Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
			override fun encode(buffer: ByteBuf, value: Vec3) {
				buffer.writeDouble(value.x)
				buffer.writeDouble(value.y)
				buffer.writeDouble(value.z)
			}
		}

	val STACK_LIST_STREAM_CODEC: StreamCodec<ByteBuf, NonNullList<ItemStack>> =
		ByteBufCodecs.fromCodec(NonNullList.codecOf(ItemStack.OPTIONAL_CODEC))

	fun <T> tagKeyStreamCodec(registry: ResourceKey<out Registry<T>>): StreamCodec<ByteBuf, TagKey<T>> {
		return ResourceLocation.STREAM_CODEC.map(
			{ TagKey.create(registry, it) },
			{ it.location() }
		)
	}

	val VECTOR2D_CODEC: Codec<Vector2d> =
		Codec.DOUBLE
			.listOf()
			.comapFlatMap(
				{ list -> Util.fixedSize(list, 2).map { Vector2d(it[0], it[1]) } },
				{ vector -> listOf(vector.x, vector.y) }
			)

	val VECTOR2D_STREAM_CODEC: StreamCodec<ByteBuf, Vector2d> =
		object : StreamCodec<ByteBuf, Vector2d> {
			override fun decode(buffer: ByteBuf): Vector2d =
				Vector2d(buffer.readDouble(), buffer.readDouble())

			override fun encode(buffer: ByteBuf, value: Vector2d) {
				buffer.writeDouble(value.x)
				buffer.writeDouble(value.y)
			}
		}

	val UINT_CODEC: Codec<UInt> =
		Codec.INT.xmap(Int::toUInt, UInt::toInt)

	val UINT_STREAM_CODEC: StreamCodec<ByteBuf, UInt> =
		ByteBufCodecs.VAR_INT.map(Int::toUInt, UInt::toInt)

}
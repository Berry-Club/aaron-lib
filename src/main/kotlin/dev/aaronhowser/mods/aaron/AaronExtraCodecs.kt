package dev.aaronhowser.mods.aaron

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
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

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

	val VEC2_CODEC: Codec<Vec2> =
		Codec.FLOAT
			.listOf()
			.comapFlatMap(
				{ list -> Util.fixedSize(list, 2).map { innerList -> Vec2(innerList[0], innerList[1]) } },
				{ vec2 -> listOf(vec2.x, vec2.y) }
			)

	val VEC2_STREAM_CODEC =
		object : StreamCodec<ByteBuf, Vec2> {
			override fun decode(buffer: ByteBuf): Vec2 = Vec2(buffer.readFloat(), buffer.readFloat())
			override fun encode(buffer: ByteBuf, value: Vec2) {
				buffer.writeFloat(value.x)
				buffer.writeFloat(value.y)
			}
		}


}
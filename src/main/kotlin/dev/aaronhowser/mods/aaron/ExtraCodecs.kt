package dev.aaronhowser.mods.aaron

import io.netty.buffer.ByteBuf
import net.minecraft.core.NonNullList
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

object ExtraCodecs {

	val VEC3_STREAM_CODEC: StreamCodec<ByteBuf, Vec3> = object : StreamCodec<ByteBuf, Vec3> {
		override fun decode(buffer: ByteBuf): Vec3 = Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
		override fun encode(buffer: ByteBuf, value: Vec3) {
			buffer.writeDouble(value.x)
			buffer.writeDouble(value.y)
			buffer.writeDouble(value.z)
		}
	}

	val STACK_LIST_STREAM_CODEC: StreamCodec<ByteBuf, NonNullList<ItemStack>> =
		ByteBufCodecs.fromCodec(NonNullList.codecOf(ItemStack.OPTIONAL_CODEC))

}
package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

class NotEntityPredicate(val other: EntityPredicate) : EntityPredicate {

	override fun test(entitySnapshot: EntitySnapshot): Boolean = !other.test(entitySnapshot)

	override fun getType(): EntityPredicate.Type = EntityPredicate.Type.NOT

	companion object {
		val CODEC: MapCodec<NotEntityPredicate> =
			EntityPredicate.CODEC
				.fieldOf("not")
				.xmap(::NotEntityPredicate, NotEntityPredicate::other)

		val STREAM_CODEC: StreamCodec<ByteBuf, NotEntityPredicate> =
			StreamCodec.composite(
				EntityPredicate.STREAM_CODEC, NotEntityPredicate::other,
				::NotEntityPredicate
			)
	}
}
package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class OrEntityPredicate(val anyOf: List<EntityPredicate>) : EntityPredicate {

	constructor(vararg predicates: EntityPredicate) : this(predicates.toList())

	override fun test(entitySnapshot: EntitySnapshot): Boolean {
		return anyOf.any { it.test(entitySnapshot) }
	}

	override fun getType(): EntityPredicate.Type = EntityPredicate.Type.OR

	companion object {
		val CODEC: MapCodec<OrEntityPredicate> =
			EntityPredicate.CODEC
				.listOf()
				.fieldOf("or")
				.xmap(::OrEntityPredicate, OrEntityPredicate::anyOf)

		val STREAM_CODEC: StreamCodec<ByteBuf, OrEntityPredicate> =
			StreamCodec.composite(
				EntityPredicate.STREAM_CODEC.apply(ByteBufCodecs.list()), OrEntityPredicate::anyOf,
				::OrEntityPredicate
			)
	}
}
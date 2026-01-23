package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity

class AndEntityPredicate(val allOf: List<EntityPredicate>) : EntityPredicate {

	constructor(vararg predicates: EntityPredicate) : this(predicates.toList())

	override fun test(entity: EntitySnapshot): Boolean {
		return allOf.all { it.test(entity) }
	}

	override fun getType(): EntityPredicate.Type = EntityPredicate.Type.AND

	companion object {
		val CODEC: MapCodec<AndEntityPredicate> =
			EntityPredicate.CODEC
				.listOf()
				.fieldOf("and")
				.xmap(::AndEntityPredicate, AndEntityPredicate::allOf)

		val STREAM_CODEC: StreamCodec<ByteBuf, AndEntityPredicate> =
			StreamCodec.composite(
				EntityPredicate.STREAM_CODEC.apply(ByteBufCodecs.list()), AndEntityPredicate::allOf,
				::AndEntityPredicate
			)
	}
}
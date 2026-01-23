package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.EntityPredicate.Type
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

object AlwaysEntityPredicate : EntityPredicate {

	override fun test(entitySnapshot: EntitySnapshot): Boolean = true
	override fun getType(): Type = Type.ALWAYS

	override fun getPassingSnapshot(): EntitySnapshot = EntitySnapshot(null, null, null, null, emptyMap())

	val CODEC: MapCodec<AlwaysEntityPredicate> = MapCodec.unit { AlwaysEntityPredicate }
	val STREAM_CODEC: StreamCodec<ByteBuf, AlwaysEntityPredicate> = StreamCodec.unit(AlwaysEntityPredicate)
}
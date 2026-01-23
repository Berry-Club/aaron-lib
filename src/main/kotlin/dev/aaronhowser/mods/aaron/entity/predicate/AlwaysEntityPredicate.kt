package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.EntityPredicate.Type
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity

object AlwaysEntityPredicate : EntityPredicate {
	override fun test(entity: Entity?) = true
	override fun getType(): Type = Type.ALWAYS

	val CODEC: MapCodec<AlwaysEntityPredicate> = MapCodec.unit { AlwaysEntityPredicate }
	val STREAM_CODEC: StreamCodec<ByteBuf, AlwaysEntityPredicate> = StreamCodec.unit(AlwaysEntityPredicate)
}
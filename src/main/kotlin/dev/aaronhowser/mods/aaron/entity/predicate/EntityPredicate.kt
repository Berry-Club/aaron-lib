package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity

sealed interface EntityPredicate {

	fun matches(entity: Entity?): Boolean
	fun getType(): Type

	companion object {
		val CODEC: Codec<EntityPredicate> =
			Type.CODEC.dispatch(
				EntityPredicate::getType,
				Type::codec
			)

		val STREAM_CODEC: StreamCodec<ByteBuf, EntityPredicate> =
			Type.STREAM_CODEC.dispatch(
				EntityPredicate::getType,
				Type::streamCodec
			)
	}

	enum class Type(
		val id: String,
		val codec: MapCodec<out EntityPredicate>,
		val streamCodec: StreamCodec<ByteBuf, out EntityPredicate>
	) : StringRepresentable {
		ALWAYS("always", AlwaysEntityPredicate.CODEC, AlwaysEntityPredicate.STREAM_CODEC),
		AND("and", AndEntityPredicate.CODEC, AndEntityPredicate.STREAM_CODEC),
		NOT("not", NotEntityPredicate.CODEC, NotEntityPredicate.STREAM_CODEC),
		OR("or", OrEntityPredicate.CODEC, OrEntityPredicate.STREAM_CODEC),
		DETAILED("detailed", DetailedEntityPredicate.CODEC, DetailedEntityPredicate.STREAM_CODEC)
		;

		override fun getSerializedName(): String = this.id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Type> = StringRepresentable.fromEnum(Type::values)
			val STREAM_CODEC: StreamCodec<ByteBuf, Type> = ByteBufCodecs.fromCodec(CODEC)
		}
	}

}
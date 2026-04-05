package dev.aaronhowser.mods.aaron.entity.predicate

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import java.util.function.Supplier

sealed interface EntityPredicate {

	fun test(entitySnapshot: EntitySnapshot): Boolean
	fun test(entity: Entity): Boolean = test(EntitySnapshot.fromEntity(entity, emptyList()))
	fun getType(): Type

	companion object {
		val CODEC: Codec<EntityPredicate> =
			Type.CODEC.dispatch(
				"predicate_type",
				EntityPredicate::getType
			) { it.codec.get() }


		val STREAM_CODEC: StreamCodec<ByteBuf, EntityPredicate> =
			Type.STREAM_CODEC.dispatch(
				EntityPredicate::getType
			) { it.streamCodec.get() }
	}

	enum class Type(
		val id: String,
		val codec: Supplier<MapCodec<out EntityPredicate>>,
		val streamCodec: Supplier<StreamCodec<ByteBuf, out EntityPredicate>>
	) : StringRepresentable {
		ALWAYS("always", { AlwaysEntityPredicate.CODEC }, { AlwaysEntityPredicate.STREAM_CODEC }),
		AND("and", { AndEntityPredicate.CODEC }, { AndEntityPredicate.STREAM_CODEC }),
		NOT("not", { NotEntityPredicate.CODEC }, { NotEntityPredicate.STREAM_CODEC }),
		OR("or", { OrEntityPredicate.CODEC }, { OrEntityPredicate.STREAM_CODEC }),
		DETAILED("detailed", { DetailedEntityPredicate.CODEC }, { DetailedEntityPredicate.STREAM_CODEC })
		;

		override fun getSerializedName(): String = this.id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Type> = StringRepresentable.fromEnum(Type::values)
			val STREAM_CODEC: StreamCodec<ByteBuf, Type> = AaronExtraStreamCodecs.enumStreamCodec(Type::class.java)
		}
	}

}
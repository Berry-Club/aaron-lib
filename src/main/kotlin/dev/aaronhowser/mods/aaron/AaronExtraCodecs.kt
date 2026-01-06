package dev.aaronhowser.mods.aaron

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.Util
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import org.joml.Vector2d
import java.util.*

object AaronExtraCodecs {

	val VECTOR2D_CODEC: Codec<Vector2d> =
		Codec.DOUBLE
			.listOf()
			.comapFlatMap(
				{ list -> Util.fixedSize(list, 2).map { Vector2d(it[0], it[1]) } },
				{ vector -> listOf(vector.x, vector.y) }
			)

	val UINT_CODEC: Codec<UInt> =
		Codec.INT.xmap(Int::toUInt, UInt::toInt)

	val COMPONENT_CODEC: Codec<Component> =
		Codec.STRING
			.xmap(
				{ str -> Component.Serializer.fromJson(str) },
				{ comp -> Component.Serializer.toJson(comp) }
			)

	val ATTRIBUTE_MODIFIER_CODEC: Codec<AttributeModifier> =
		RecordCodecBuilder.create { instance ->
			instance.group(
				UUIDUtil.CODEC
					.optionalFieldOf("uuid")
					.forGetter { modifier -> Optional.of(modifier.id) },
				Codec.STRING
					.fieldOf("name")
					.forGetter(AttributeModifier::getName),
				Codec.DOUBLE
					.fieldOf("amount")
					.forGetter(AttributeModifier::getAmount),
				Codec.INT
					.fieldOf("operation")
					.forGetter { modifier -> modifier.operation.toValue() }
			).apply(instance) { uuid, name, amount, operationValue ->
				if (uuid.isPresent) {
					AttributeModifier(uuid.get(), name, amount, AttributeModifier.Operation.fromValue(operationValue))
				} else {
					AttributeModifier(name, amount, AttributeModifier.Operation.fromValue(operationValue))
				}
			}
		}

}
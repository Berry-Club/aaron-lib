package dev.aaronhowser.mods.aaron.misc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.*
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class ImprovedEntityPredicate(
	val entityType: Optional<EntityTypePredicate> = Optional.empty(),
	val movement: Optional<MovementPredicate> = Optional.empty(),
	val effects: Optional<MobEffectsPredicate> = Optional.empty(),
	val nbt: Optional<NbtPredicate> = Optional.empty(),
	val flags: Optional<EntityFlagsPredicate> = Optional.empty(),
	val periodicTick: Optional<Int> = Optional.empty(),
	val vehicle: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val passenger: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val targetedEntity: Optional<ImprovedEntityPredicate> = Optional.empty(),
	val team: Optional<String> = Optional.empty(),
	val slots: Optional<SlotsPredicate> = Optional.empty()
) {

	companion object {
		val CODEC: Codec<ImprovedEntityPredicate> =
			Codec.recursive("ImprovedEntityPredicate") { codec ->
				RecordCodecBuilder.create { instance ->
					instance.group(
						EntityTypePredicate.CODEC
							.optionalFieldOf("entity_type")
							.forGetter(ImprovedEntityPredicate::entityType),
						MovementPredicate.CODEC
							.optionalFieldOf("movement")
							.forGetter(ImprovedEntityPredicate::movement),
						MobEffectsPredicate.CODEC
							.optionalFieldOf("effects")
							.forGetter(ImprovedEntityPredicate::effects),
						NbtPredicate.CODEC
							.optionalFieldOf("nbt")
							.forGetter(ImprovedEntityPredicate::nbt),
						EntityFlagsPredicate.CODEC
							.optionalFieldOf("flags")
							.forGetter(ImprovedEntityPredicate::flags),
						Codec.INT
							.optionalFieldOf("periodic_tick")
							.forGetter(ImprovedEntityPredicate::periodicTick),
						codec
							.optionalFieldOf("vehicle")
							.forGetter(ImprovedEntityPredicate::vehicle),
						codec
							.optionalFieldOf("passenger")
							.forGetter(ImprovedEntityPredicate::passenger),
						codec
							.optionalFieldOf("targeted_entity")
							.forGetter(ImprovedEntityPredicate::targetedEntity),
						Codec.STRING
							.optionalFieldOf("team")
							.forGetter(ImprovedEntityPredicate::team),
						SlotsPredicate.CODEC
							.optionalFieldOf("slots")
							.forGetter(ImprovedEntityPredicate::slots)
					).apply(instance, ::ImprovedEntityPredicate)
				}
			}

		// FIXME
		val STREAM_CODEC: StreamCodec<ByteBuf, ImprovedEntityPredicate> =
			ByteBufCodecs.fromCodec(CODEC)
	}
}
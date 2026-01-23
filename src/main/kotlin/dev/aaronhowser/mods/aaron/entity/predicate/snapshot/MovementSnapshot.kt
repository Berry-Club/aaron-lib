package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.serialization.AaronExtraCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.MovementPredicate
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3

class MovementSnapshot(
	val deltaMovement: Vec3,
	val fallDistance: Double
) {

	fun test(predicate: MovementPredicate): Boolean {
		return predicate.matches(
			deltaMovement.x,
			deltaMovement.y,
			deltaMovement.z,
			fallDistance
		)
	}

	companion object {
		val CODEC: Codec<MovementSnapshot> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Vec3.CODEC
						.fieldOf("delta_movement")
						.forGetter(MovementSnapshot::deltaMovement),
					Codec.DOUBLE
						.fieldOf("fall_distance")
						.forGetter(MovementSnapshot::fallDistance)
				).apply(instance, ::MovementSnapshot)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, MovementSnapshot> =
			StreamCodec.composite(
				AaronExtraCodecs.VEC3_STREAM_CODEC, MovementSnapshot::deltaMovement,
				ByteBufCodecs.DOUBLE, MovementSnapshot::fallDistance,
				::MovementSnapshot
			)
	}

}
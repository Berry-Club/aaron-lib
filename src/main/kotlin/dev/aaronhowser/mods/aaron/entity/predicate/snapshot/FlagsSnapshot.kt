package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.EntityFlagsPredicate
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class FlagsSnapshot(
	val isOnGround: Boolean,
	val isOnFire: Boolean,
	val isCrouching: Boolean,
	val isSprinting: Boolean,
	val isSwimming: Boolean,
) {

	fun test(predicate: EntityFlagsPredicate): Boolean {
		if (predicate.isOnGround.isPresent && predicate.isOnGround.get() != isOnGround) return false
		if (predicate.isOnFire.isPresent && predicate.isOnFire.get() != isOnFire) return false
		if (predicate.isCrouching.isPresent && predicate.isCrouching.get() != isCrouching) return false
		if (predicate.isSprinting.isPresent && predicate.isSprinting.get() != isSprinting) return false
		if (predicate.isSwimming.isPresent && predicate.isSwimming.get() != isSwimming) return false
		return true
	}

	companion object {
		val CODEC: Codec<FlagsSnapshot> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.BOOL
						.optionalFieldOf("on_ground", true)
						.forGetter(FlagsSnapshot::isOnGround),
					Codec.BOOL
						.optionalFieldOf("on_fire", false)
						.forGetter(FlagsSnapshot::isOnFire),
					Codec.BOOL
						.optionalFieldOf("crouching", false)
						.forGetter(FlagsSnapshot::isCrouching),
					Codec.BOOL
						.optionalFieldOf("sprinting", false)
						.forGetter(FlagsSnapshot::isSprinting),
					Codec.BOOL
						.optionalFieldOf("swimming", false)
						.forGetter(FlagsSnapshot::isSwimming)
				).apply(instance, ::FlagsSnapshot)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, FlagsSnapshot> =
			StreamCodec.composite(
				ByteBufCodecs.BOOL, FlagsSnapshot::isOnGround,
				ByteBufCodecs.BOOL, FlagsSnapshot::isOnFire,
				ByteBufCodecs.BOOL, FlagsSnapshot::isCrouching,
				ByteBufCodecs.BOOL, FlagsSnapshot::isSprinting,
				ByteBufCodecs.BOOL, FlagsSnapshot::isSwimming,
				::FlagsSnapshot
			)

		fun fromPredicate(predicate: EntityFlagsPredicate): FlagsSnapshot {
			return FlagsSnapshot(
				isOnGround = predicate.isOnGround.orElse(true),
				isOnFire = predicate.isOnFire.orElse(false),
				isCrouching = predicate.isCrouching.orElse(false),
				isSprinting = predicate.isSprinting.orElse(false),
				isSwimming = predicate.isSwimming.orElse(false)
			)
		}
	}

}
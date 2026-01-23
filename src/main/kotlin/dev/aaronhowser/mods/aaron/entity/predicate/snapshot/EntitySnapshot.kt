package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import java.util.*

data class EntitySnapshot(
	val entityType: EntityType<*>? = null,
	val nbtSnapshot: NbtSnapshot? = null,
	val flagsSnapshot: FlagsSnapshot? = null,
	val movementSnapshot: MovementSnapshot? = null,
	val activeEffects: Map<Holder<MobEffect>, MobEffectInstance> = emptyMap()
) {

	companion object {

		val CODEC: Codec<EntitySnapshot> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec()
						.optionalFieldOf("entity_type", null)
						.forGetter(EntitySnapshot::entityType),
					NbtSnapshot.CODEC
						.optionalFieldOf("nbt", null)
						.forGetter(EntitySnapshot::nbtSnapshot),
					FlagsSnapshot.CODEC
						.optionalFieldOf("flags", null)
						.forGetter(EntitySnapshot::flagsSnapshot),
					MovementSnapshot.CODEC
						.optionalFieldOf("movement", null)
						.forGetter(EntitySnapshot::movementSnapshot),
					Codec.unboundedMap(
						BuiltInRegistries.MOB_EFFECT.holderByNameCodec(),
						MobEffectInstance.CODEC
					)
						.optionalFieldOf("active_effects", emptyMap())
						.forGetter(EntitySnapshot::activeEffects)
				).apply(instance, ::EntitySnapshot)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EntitySnapshot> =
			StreamCodec.composite(
				ByteBufCodecs.optional(ByteBufCodecs.registry(Registries.ENTITY_TYPE)), { Optional.ofNullable(it.entityType) },
				ByteBufCodecs.optional(NbtSnapshot.STREAM_CODEC), { Optional.ofNullable(it.nbtSnapshot) },
				ByteBufCodecs.optional(FlagsSnapshot.STREAM_CODEC), { Optional.ofNullable(it.flagsSnapshot) },
				ByteBufCodecs.optional(MovementSnapshot.STREAM_CODEC), { Optional.ofNullable(it.movementSnapshot) },
				ByteBufCodecs.map(
					::HashMap,
					ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
					MobEffectInstance.STREAM_CODEC
				),
				EntitySnapshot::activeEffects,
			) { entityType,
				nbt,
				flags,
				movement,
				effects ->
				EntitySnapshot(
					entityType.orElse(null),
					nbt.orElse(null),
					flags.orElse(null),
					movement.orElse(null),
					effects
				)
			}

		fun fromEntity(entity: Entity, includeNbtKeys: List<String>): EntitySnapshot {
			val nbtSnapshot = NbtSnapshot.fromEntity(entity, includeNbtKeys)

			val flagsSnapshot = FlagsSnapshot(
				isOnGround = entity.onGround(),
				isOnFire = entity.isOnFire,
				isCrouching = entity.isCrouching,
				isSprinting = entity.isSprinting,
				isSwimming = entity.isSwimming
			)

			val movementSnapshot = MovementSnapshot(
				deltaMovement = entity.deltaMovement.scale(20.0),
				fallDistance = entity.fallDistance.toDouble()
			)

			return EntitySnapshot(
				entity.type,
				nbtSnapshot,
				flagsSnapshot,
				movementSnapshot,
				if (entity is LivingEntity) entity.activeEffectsMap else emptyMap()
			)
		}
	}

}
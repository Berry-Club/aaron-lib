package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.critereon.NbtPredicate
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity

class NbtSnapshot(
	val tag: CompoundTag
) {

	fun test(predicate: NbtPredicate): Boolean {
		return predicate.matches(tag)
	}

	companion object {
		val CODEC: Codec<NbtSnapshot> = CompoundTag.CODEC.xmap(::NbtSnapshot, NbtSnapshot::tag)
		val STREAM_CODEC: StreamCodec<ByteBuf, NbtSnapshot> = ByteBufCodecs.COMPOUND_TAG.map(::NbtSnapshot, NbtSnapshot::tag)

		fun fromEntity(entity: Entity, includeKeys: List<String>): NbtSnapshot {
			val entityNbt = CompoundTag()
			entity.save(entityNbt)

			val filteredNbt = CompoundTag()

			for (key in includeKeys) {
				val value = entityNbt.get(key)
				if (value != null) {
					filteredNbt.put(key, value)
				}
			}

			return NbtSnapshot(filteredNbt)
		}

	}

}
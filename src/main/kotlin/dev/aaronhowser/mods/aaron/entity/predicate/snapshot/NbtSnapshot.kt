package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.advancements.criterion.NbtPredicate
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ProblemReporter
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.TagValueOutput

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
			val output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess())
			entity.save(output)
			val entityNbt = output.buildResult()

			val filteredNbt = CompoundTag()

			for (key in includeKeys) {
				val value = entityNbt.get(key)
				if (value != null) {
					filteredNbt.put(key, value)
				}
			}

			return NbtSnapshot(filteredNbt)
		}

		fun fromPredicate(predicate: NbtPredicate): NbtSnapshot {
			return NbtSnapshot(predicate.tag)
		}

	}

}

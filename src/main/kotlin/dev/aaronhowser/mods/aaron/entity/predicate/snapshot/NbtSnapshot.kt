package dev.aaronhowser.mods.aaron.entity.predicate.snapshot

import net.minecraft.advancements.critereon.NbtPredicate
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

class NbtSnapshot(
	val tag: CompoundTag
) {

	fun test(predicate: NbtPredicate): Boolean {
		return predicate.matches(tag)
	}

	companion object {

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
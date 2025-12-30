package dev.aaronhowser.mods.aaron

import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.UsernameCache
import java.util.*

object AaronUtil {

	fun getCachedUuid(playerUsername: String): UUID? {
		val map = UsernameCache.getMap()
		for ((uuid, username) in map) {
			if (username.equals(playerUsername, ignoreCase = true)) {
				return uuid
			}
		}

		return null
	}

	fun cleanEntityNbt(compoundTag: CompoundTag, stripUniqueness: Boolean = false) {
		val badTags = buildList {
			addAll(
				listOf(
					"HurtByTimestamp",
					"Sitting",
					"FallFlying",
					"PortalCooldown",
					"FallDistance",
					"InLove",
					"DeathTime",
					"ForcedAge",
					"Motion",
					"Air",
					"OnGround",
					"Rotation",
					"Pos",
					"HurtTime"
				)
			)

			if (stripUniqueness) {
				add("id")
				add("UUID")
				add("Owner")
				add("Age")
			}
		}

		for (tag in badTags) {
			compoundTag.remove(tag)
		}
	}

}
package dev.aaronhowser.mods.aaron

import net.minecraftforge.common.UsernameCache
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

}
package dev.aaronhowser.mods.aaron.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var cleanEntityNbtKeys: ModConfigSpec.ConfigValue<List<String>>
	lateinit var cleanEntityNbtKeysStripUniqueness: ModConfigSpec.ConfigValue<List<String>>

	init {
		generalConfigs()
	}

	private fun generalConfigs() {
		cleanEntityNbtKeys = builder
			.comment("A list of NBT keys that will be removed by `AaronUtil#cleanEntityNbt`.")
			.defineListAllowEmpty(
				"cleanEntityNbtKeys",
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
				),
				{ it is String }
			)

		cleanEntityNbtKeysStripUniqueness = builder
			.comment("A list of NBT keys that will be removed by `AaronUtil#cleanEntityNbt` when `stripUniqueness` is set to true.")
			.defineListAllowEmpty(
				"cleanEntityNbtKeysStripUniqueness",
				listOf(
					"id",
					"UUID",
					"Owner",
					"Age"
				),
				{ it is String }
			)
	}

	companion object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> =
			ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}
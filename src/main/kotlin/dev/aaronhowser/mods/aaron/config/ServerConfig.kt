package dev.aaronhowser.mods.aaron.config

import net.minecraftforge.common.ForgeConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ForgeConfigSpec.Builder
) {

	lateinit var cleanEntityNbtKeys: ForgeConfigSpec.ConfigValue<List<String>>
	lateinit var cleanEntityNbtKeysStripUniqueness: ForgeConfigSpec.ConfigValue<List<String>>

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
		private val configPair: Pair<ServerConfig, ForgeConfigSpec> =
			ForgeConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ForgeConfigSpec = configPair.right
	}

}
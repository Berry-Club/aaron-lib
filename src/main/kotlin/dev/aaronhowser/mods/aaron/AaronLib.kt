package dev.aaronhowser.mods.aaron

import dev.aaronhowser.mods.aaron.registry.actual.AaronLootPoolEntryTypes
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(AaronLib.MOD_ID)
class AaronLib(
	modContainer: ModContainer
) {

	init {
		LOGGER.log(Level.INFO, "Aaron.")
		LOGGER.log(Level.INFO, "Arriving.")
		LOGGER.log(Level.INFO, "On the scene.")

		AaronLootPoolEntryTypes.LOOT_POOL_ENTRY_TYPE_REGISTRY.register(MOD_BUS)
	}

	companion object {
		const val MOD_ID = "aaron"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}

}
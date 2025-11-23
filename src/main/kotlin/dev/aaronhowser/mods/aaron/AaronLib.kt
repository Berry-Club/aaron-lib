package dev.aaronhowser.mods.aaron

import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(AaronLib.MOD_ID)
class AaronLib(
	modContainer: ModContainer
) {

	init {
		LOGGER.log(Level.INFO, "Aaron.")
		LOGGER.log(Level.INFO, "Arriving.")
		LOGGER.log(Level.INFO, "On the scene.")
	}

	companion object {
		const val MOD_ID = "aaron"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)
	}

}
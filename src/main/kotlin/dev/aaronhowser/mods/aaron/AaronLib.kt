package dev.aaronhowser.mods.aaron

import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

@Mod(AaronLib.MOD_ID)
class AaronLib {

	init {
		LOGGER.log(Level.INFO, "Aaron.")
		LOGGER.log(Level.INFO, "Arriving.")
		LOGGER.log(Level.INFO, "On the scene.")

		val attributeModifier = AttributeModifier(UUID.randomUUID(), "name", 1.0, AttributeModifier.Operation.ADDITION)
		val s = AaronExtraCodecs.ATTRIBUTE_MODIFIER_CODEC.encodeStart(JsonOps.INSTANCE, attributeModifier)
		println(s.getOrThrow(false, {}).toString())
	}

	companion object {
		const val MOD_ID = "aaron"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)
	}

}
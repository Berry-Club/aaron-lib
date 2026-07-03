package dev.aaronhowser.mods.aaron.client

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.material.Fluid

object AaronClientUtil {

	val FLUID_COLORS: MutableMap<Fluid, Int> = mutableMapOf()

	@JvmStatic
	val localLevel: Level?
		get() = Minecraft.getInstance().level

	@JvmStatic
	val localPlayer: Player?
		get() = Minecraft.getInstance().player

	fun getBiomeDisplay(biomeKey: ResourceKey<Biome>): MutableComponent {
		val probableTranslationKey = "biome.${biomeKey.identifier().namespace}.${biomeKey.identifier().path}"
		val hasTranslation = I18n.exists(probableTranslationKey)

		return if (hasTranslation) {
			Component.translatable(probableTranslationKey)
		} else {
			Component.literal(biomeKey.identifier().toString())
		}
	}

	fun getFluidColor(fluid: Fluid): Int {
		val existing = FLUID_COLORS[fluid]
		if (existing != null) return existing

		val new = computeFluidColor(fluid)
		FLUID_COLORS[fluid] = new
		return new
	}

	fun computeFluidColor(fluid: Fluid): Int {
		return 0xFFFFFFFF.toInt()
	}

}

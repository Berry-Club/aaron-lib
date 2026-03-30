package dev.aaronhowser.mods.aaron.client

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.neoforged.fml.loading.FMLEnvironment

object AaronClientUtil {

	@JvmStatic
	val localLevel: Level?
		get() {
			if (!FMLEnvironment.dist.isClient) return null
			return Minecraft.getInstance().level
		}

	@JvmStatic
	val localPlayer: Player?
		get() {
			if (!FMLEnvironment.dist.isClient) return null
			return Minecraft.getInstance().player
		}

	fun getBiomeDisplay(biomeKey: ResourceKey<Biome>): MutableComponent {
		val probableTranslationKey = "biome.${biomeKey.location().namespace}.${biomeKey.location().path}"
		val hasTranslation = I18n.exists(probableTranslationKey)

		return if (hasTranslation) {
			Component.translatable(probableTranslationKey)
		} else {
			Component.literal(biomeKey.location().toString())
		}
	}

}
package dev.aaronhowser.mods.aaron.client

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.language.I18n
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.util.ARGB
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

object AaronClientUtil {

	val FLUID_COLORS: MutableMap<Fluid, Int> = mutableMapOf()

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

	fun getFluidColor(fluid: Fluid): Int {
		val existing = FLUID_COLORS[fluid]
		if (existing != null) return existing

		val new = computeFluidColor(fluid)
		FLUID_COLORS[fluid] = new
		return new
	}

	fun computeFluidColor(fluid: Fluid): Int {
		val ext = IClientFluidTypeExtensions.of(fluid)
		val tintColor = ext.tintColor
		if (tintColor != 0xFFFFFFFF.toInt()) {
			return tintColor
		}

		val textureLocation = ext.stillTexture
		val atlasTexture = Minecraft.getInstance()
			.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
			.apply(textureLocation)

		if (atlasTexture == null) {
			return 0xFFFFFFFF.toInt()
		}

		val pixels = buildList {
			val contents = atlasTexture.contents()
			val width = contents.width()
			val height = contents.height()
			val nativeImage = contents.originalImage

			for (x in 0 until width) {
				for (y in 0 until height) {
					val argb = nativeImage.getPixel(x, y)
					val alpha = ARGB.alpha(argb)
					if (alpha > 10) {
						add(argb)
					}
				}
			}
		}

		if (pixels.isEmpty()) {
			return 0xFFFFFFFF.toInt()
		}

		var totalRed = 0L
		var totalGreen = 0L
		var totalBlue = 0L

		for (argb in pixels) {
			val red = ARGB.red(argb)
			val green = ARGB.green(argb)
			val blue = ARGB.blue(argb)

			totalRed += red
			totalGreen += green
			totalBlue += blue
		}

		val count = pixels.size
		val averageRed = (totalRed / count).toInt()
		val averageGreen = (totalGreen / count).toInt()
		val averageBlue = (totalBlue / count).toInt()

		return ARGB.color(averageRed, averageGreen, averageBlue)
	}

}
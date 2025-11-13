package dev.aaronhowser.mods.aaron.client

import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
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

}
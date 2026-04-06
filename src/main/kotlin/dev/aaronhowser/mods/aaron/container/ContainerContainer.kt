package dev.aaronhowser.mods.aaron.container

import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.level.Level

interface ContainerContainer {

	fun getContainers(): List<Container>

	fun dropContents(level: Level, pos: BlockPos) {
		for (container in getContainers()) {
			Containers.dropContents(level, pos, container)
		}

	}
}
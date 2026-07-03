package dev.aaronhowser.mods.aaron.block

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class SimpleContainerBlock(properties: Properties) : Block(properties) {

	override fun affectNeighborsAfterRemoval(state: BlockState, level: ServerLevel, pos: BlockPos, movedByPiston: Boolean) {
		val be = level.getBlockEntity(pos)
		if (be is ContainerContainer) {
			be.dropContents(level, pos)
		}

		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston)
	}

}

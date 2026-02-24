package dev.aaronhowser.mods.aaron.block_walker

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import java.util.function.Predicate

// Largely copied from VidLib
interface BlockFilter : Predicate<BlockInWorld> {

	fun test(level: Level, pos: BlockPos, state: BlockState): Boolean {
		if (this == NONE) return false
	}

	companion object {
		val NONE =
			object : BlockFilter {
				override fun test(blockInWorld: BlockInWorld): Boolean = false
				override fun test(level: Level, pos: BlockPos, state: BlockState): Boolean = false
			}
	}

}
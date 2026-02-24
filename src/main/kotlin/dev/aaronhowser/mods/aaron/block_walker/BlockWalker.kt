package dev.aaronhowser.mods.aaron.block_walker

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

// Largely copied from VidLib
class BlockWalker(
	private val level: Level,
	private val walkType: WalkType,
	startPos: BlockPos,
	private val filter: (BlockState) -> Boolean = { true },
	private val maxDistance: Int,
	private val maxTotalBlocks: Int,
	private val onFinished: (List<ConnectedBlock>) -> Unit
) {

	private val visited = LongOpenHashSet()
	private val queue = ArrayDeque<ConnectedBlock>()
	private val collectedResults = Long2ObjectOpenHashMap<ConnectedBlock>()

	private var isFinished = false

	init {
		queue.add(
			ConnectedBlock(
				PositionedBlock(startPos, level.getBlockState(startPos)),
				0,
			)
		)
	}

	fun start(maxIterationsPerTick: Int) {
		if (isFinished) return
		tick(maxIterationsPerTick)
	}

	private fun tick(maxIterationsPerTick: Int) {
		if (isFinished) return
	}

}
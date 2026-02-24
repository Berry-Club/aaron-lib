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
	private val filter: (Level, BlockPos, BlockState) -> Boolean = { l, b, s -> true },
	private val maxDistance: Int,
	private val maxTotalBlocks: Int,
	private val shouldStop: (ConnectedBlock) -> Boolean = { false },
	private val onFinished: (List<ConnectedBlock>) -> Unit
) {

	private val visited = LongOpenHashSet()
	private val pendingQueue = ArrayDeque<ConnectedBlock>()
	private val collectedResults = Long2ObjectOpenHashMap<ConnectedBlock>()

	private var isFinished = false

	init {
		pendingQueue.add(
			ConnectedBlock(
				PositionedBlock(startPos, level.getBlockState(startPos)),
				0,
			)
		)

		visited.add(startPos.asLong())
	}

	fun start(maxIterationsPerTick: Int) {
		if (isFinished) return
		tick(maxIterationsPerTick)
	}

	private fun tick(maxIterationsPerTick: Int) {
		if (isFinished) return

		var iterationsThisTick = 0

		while (pendingQueue.isNotEmpty() && iterationsThisTick++ < maxIterationsPerTick) {
			val current = pendingQueue.removeFirst()

			if (current.distance != 0) {
				if (!filter(level, current.block.pos, current.block.state)) {
					continue
				}
			}

			collectedResults[current.block.pos.asLong()] = current

			if (shouldStop(current) || collectedResults.size >= maxTotalBlocks) {
				finish()
				return
			}

			if (current.distance + 1 > maxDistance) continue

			for (offset in walkType.neighborOffsets) {
				val neighborPos = current.block.pos.offset(offset)
				val neighborState = level.getBlockState(neighborPos)

				if (visited.add(neighborPos.asLong()) && filter(level, neighborPos, neighborState)) {

				}
			}

		}
	}

	private fun finish() {
		if (isFinished) return
		isFinished = true

		onFinished(collectedResults.values.toList())
	}

}
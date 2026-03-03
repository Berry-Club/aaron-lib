package dev.aaronhowser.mods.aaron.block_walker

import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
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
	private val filter: BlockFilter = BlockFilter { _, _, _ -> true },
	private val maxDistance: Int,
	private val maxTotalBlocks: Int,
	private val shouldStop: ShouldStopPredicate = ShouldStopPredicate { false },
	private val onWalked: OnWalkedConsumer = OnWalkedConsumer {},
	private val onFinished: OnFinishedConsumer = OnFinishedConsumer {}
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
				if (!filter.test(level, current.block.pos, current.block.state)) {
					continue
				}
			}

			collectedResults[current.block.pos.asLong()] = current

			onWalked.accept(current)

			if (shouldStop.test(current) || collectedResults.size >= maxTotalBlocks) {
				finish()
				return
			}

			if (current.distance + 1 > maxDistance) continue

			for (offset in walkType.neighborOffsets) {
				val neighborPos = current.block.pos.offset(offset)
				val neighborState = level.getBlockState(neighborPos)

				if (visited.add(neighborPos.asLong()) && filter.test(level, neighborPos, neighborState)) {
					pendingQueue.add(
						ConnectedBlock(
							PositionedBlock(neighborPos, neighborState),
							current.distance + 1
						)
					)
				}
			}
		}

		if (pendingQueue.isEmpty()) {
			finish()
		} else {
			level.scheduleTaskInTicks(1) { tick(maxIterationsPerTick) }
		}
	}

	private fun finish() {
		if (isFinished) return
		isFinished = true

		onFinished.accept(collectedResults.values.toList())
	}

	fun interface BlockFilter {
		/**
		 * Ran on every block before it's added to the queue.
		 * @return `true` to add the block to the queue, `false` to skip it.
		 */
		fun test(level: Level, pos: BlockPos, state: BlockState): Boolean
	}

	fun interface ShouldStopPredicate {
		/**
		 * Ran on every block in the queue after it's walked. This always matches the filter.
		 * @return `true` to stop the entire walk immediately, `false` to continue as normal.
		 */
		fun test(block: ConnectedBlock): Boolean
	}

	fun interface OnWalkedConsumer {
		/**
		 * Ran on every block in the queue after it's walked. This always matches the filter.
		 * This allows you to do something with the block as soon as it's walked, rather than waiting until the end of the entire walk.
		 */
		fun accept(block: ConnectedBlock)
	}

	fun interface OnFinishedConsumer {
		/**
		 * Ran once after the walk is finished, either because there are no more blocks to walk or because `shouldStop` returned `true`.
		 */
		fun accept(blocks: List<ConnectedBlock>)
	}

}
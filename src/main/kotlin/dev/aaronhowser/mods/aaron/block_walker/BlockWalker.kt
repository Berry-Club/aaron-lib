package dev.aaronhowser.mods.aaron.block_walker

import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

// Largely copied from VidLib
class BlockWalker(
	private val level: Level,
	private val searchOffsets: List<Vec3i>,
	startPos: BlockPos,
	private val searchFromTail: Boolean = false,
	private val filter: BlockFilter = BlockFilter { _, _, _ -> true },
	private val maxDistance: Int,
	private val maxTotalBlocks: Int,
	private val shouldStop: ShouldStopPredicate = ShouldStopPredicate { false },
	private val onWalked: OnWalkedConsumer = OnWalkedConsumer {},
	private val onFinished: OnFinishedConsumer = OnFinishedConsumer {}
) {

	constructor(
		level: Level,
		walkType: WalkType,
		startPos: BlockPos,
		searchFromTail: Boolean = false,
		filter: BlockFilter = BlockFilter { _, _, _ -> true },
		maxDistance: Int,
		maxTotalBlocks: Int,
		shouldStop: ShouldStopPredicate = ShouldStopPredicate { false },
		onWalked: OnWalkedConsumer = OnWalkedConsumer {},
		onFinished: OnFinishedConsumer = OnFinishedConsumer {}
	) : this(
		level,
		walkType.neighborOffsets,
		startPos,
		searchFromTail,
		filter,
		maxDistance,
		maxTotalBlocks,
		shouldStop,
		onWalked,
		onFinished
	)

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

	fun locateAllImmediately(): List<ConnectedBlock> {
		start(Int.MAX_VALUE)
		return collectedResults.values.toList()
	}

	private fun tick(maxIterationsPerTick: Int) {
		if (isFinished) return

		var iterationsThisTick = 0

		while (pendingQueue.isNotEmpty() && iterationsThisTick++ < maxIterationsPerTick) {
			val current = if (searchFromTail) pendingQueue.removeLast() else pendingQueue.removeFirst()

			if (!filter.test(level, current.block.pos, current.block.state)) {
				continue
			}

			collectedResults[current.block.pos.asLong()] = current

			onWalked.accept(current)

			if (shouldStop.test(current) || collectedResults.size >= maxTotalBlocks) {
				finish()
				return
			}

			if (current.distance + 1 > maxDistance) continue

			for (offset in searchOffsets) {
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

	class Builder(private val level: Level) {
		private var searchOffsets: List<Vec3i>? = null
		private var startPos: BlockPos? = null
		private var searchFromTail: Boolean = false
		private var filter: BlockFilter = BlockFilter { _, _, _ -> true }
		private var maxDistance: Int = Int.MAX_VALUE
		private var maxTotalBlocks: Int = Int.MAX_VALUE
		private var shouldStop: ShouldStopPredicate = ShouldStopPredicate { false }
		private var onWalked: OnWalkedConsumer = OnWalkedConsumer {}
		private var onFinished: OnFinishedConsumer = OnFinishedConsumer {}

		fun searchOffsets(searchOffsets: List<Vec3i>): Builder {
			this.searchOffsets = searchOffsets
			return this
		}

		fun startPos(startPos: BlockPos): Builder {
			this.startPos = startPos
			return this
		}

		fun searchFromTail(searchFromTail: Boolean): Builder {
			this.searchFromTail = searchFromTail
			return this
		}

		fun filter(filter: BlockFilter): Builder {
			this.filter = filter
			return this
		}

		fun maxDistance(maxDistance: Int): Builder {
			this.maxDistance = maxDistance
			return this
		}

		fun maxTotalBlocks(maxTotalBlocks: Int): Builder {
			this.maxTotalBlocks = maxTotalBlocks
			return this
		}

		fun shouldStop(shouldStop: ShouldStopPredicate): Builder {
			this.shouldStop = shouldStop
			return this
		}

		fun onWalked(onWalked: OnWalkedConsumer): Builder {
			this.onWalked = onWalked
			return this
		}

		fun onFinished(onFinished: OnFinishedConsumer): Builder {
			this.onFinished = onFinished
			return this
		}

		fun build(): BlockWalker {
			return BlockWalker(
				level,
				searchOffsets ?: throw IllegalStateException("Search offsets must be set"),
				startPos ?: throw IllegalStateException("Start position must be set"),
				searchFromTail,
				filter,
				maxDistance,
				maxTotalBlocks,
				shouldStop,
				onWalked,
				onFinished
			)
		}
	}

}
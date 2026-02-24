package dev.aaronhowser.mods.aaron.block_walker

import net.minecraft.core.Direction
import net.minecraft.core.Vec3i

// Copied largely from VidLib
enum class WalkType(
	includeDiagonals: Boolean,
	horizontalOnly: Boolean
) {
	ALL_CARDINAL(false, false),        // 6 neighbors
	ALL_SURROUNDING(true, false),        // 3x3x3 - 1 neighbors
	HORIZONTAL_CARDINAL(false, true),    // 4 neighbors
	HORIZONTAL_DIAGONAL(true, true)    // 8 neighbors

	;

	val neighborOffsets: List<Vec3i>

	init {
		val offsets = mutableListOf<Vec3i>()

		if (!includeDiagonals && !horizontalOnly) {
			for (dir in Direction.entries) {
				offsets.add(Vec3i(dir.stepX, dir.stepY, dir.stepZ))
			}
		} else {
			val verticalRange = if (horizontalOnly) 0..0 else -1..1

			for (dy in verticalRange) {

				if (includeDiagonals) {
					for (dx in -1..1) for (dz in -1..1) {
						if (dx == 0 && dy == 0 && dz == 0) continue
						offsets.add(Vec3i(dx, dy, dz))
					}
				} else {
					for (dir in Direction.Plane.HORIZONTAL) {
						offsets.add(Vec3i(dir.stepX, dy, dir.stepZ))
					}
				}
			}
		}

		this.neighborOffsets = offsets
	}

}
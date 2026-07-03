package dev.aaronhowser.mods.aaron.misc

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import kotlin.math.pow

@Suppress("unused")
object GeometryExtensions {
	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Vector3f.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Long.toBlockPos(): BlockPos = BlockPos.of(this)
	fun Long.toChunkPos(): ChunkPos = ChunkPos(this.toInt(), (this shr 32).toInt())

	fun BlockPos.furtherThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) > distance.toDouble().pow(2)
	fun Vec3.furtherThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) > distance.toDouble().pow(2)

	fun BlockPos.closerThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) < distance.toDouble().pow(2)
	fun Vec3.closerThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) < distance.toDouble().pow(2)

	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun RandomSource.chance(chance: Number): Boolean = nextDouble() <= chance.toDouble()

	fun RandomSource.roll(chance: Number): Boolean = nextDouble() <= chance.toDouble()
	fun RandomSource.oneIn(sides: Int): Boolean = nextInt(0, sides) == 0

	fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
	fun Number.toRadians(): Double = Math.toRadians(this.toDouble())

	fun AABB.randomX(random: RandomSource): Double = minX + (xsize * random.nextDouble())
	fun AABB.randomY(random: RandomSource): Double = minY + (ysize * random.nextDouble())
	fun AABB.randomZ(random: RandomSource): Double = minZ + (zsize * random.nextDouble())
	fun AABB.randomPos(random: RandomSource): Vec3 {
		return Vec3(randomX(random), randomY(random), randomZ(random))
	}
}

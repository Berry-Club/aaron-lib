package dev.aaronhowser.mods.aaron.misc.extensions

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

object AaronLevelExtensions {

	val Level.isServerSide: Boolean get() = !this.isClientSide

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Vector3f.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Long.toBlockPos(): BlockPos = BlockPos.of(this)
	fun Long.toChunkPos(): ChunkPos = ChunkPos.unpack(this)


}
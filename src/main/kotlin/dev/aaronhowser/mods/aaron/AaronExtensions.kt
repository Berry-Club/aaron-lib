package dev.aaronhowser.mods.aaron

import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*

object AaronExtensions {

	val Entity.isClientSide: Boolean
		get() = this.level().isClientSide

	val Level.isServerSide: Boolean
		get() = !this.isClientSide

	fun DyeColor.getDyeName(): String = this.getName()
	fun Direction.getDirectionName(): String = this.getName()
	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun Player.status(message: Component) = this.displayClientMessage(message, true)
	fun Boolean?.isTrue(): Boolean = this == true
	fun Boolean?.isNotTrue(): Boolean = this != true

	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		return if (this.hasUUID(key)) this.getUUID(key) else null
	}

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> Any?.cast(): T = this as T

}
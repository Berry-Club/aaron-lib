package dev.aaronhowser.mods.aaron.misc

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

@Suppress("unused")
object WorldEntityExtensions {
	val Level.isServerSide: Boolean get() = !this.isClientSide

	val Entity.isClientSide: Boolean get() = this.level().isClientSide
	val Entity.isServerSide: Boolean get() = this.level().isServerSide

	fun Player.status(message: Component) = this.sendOverlayMessage(message)
	fun Player.status(message: String) = this.status(Component.literal(message))

	fun LivingEntity.tell(message: Component) {
		if (this is Player) this.sendSystemMessage(message)
	}
	fun LivingEntity.tell(message: String) = this.tell(Component.literal(message))

	fun Entity.isMovingHorizontally(): Boolean {
		return this.deltaMovement.horizontalDistance() > 0.015
	}

	fun Player.getPovResult(range: Number = this.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)): BlockHitResult {
		val asEntity = this as Entity
		return asEntity.getPovResult(range)
	}

	fun Entity.getPovResult(range: Number): BlockHitResult {
		return this.level().clip(
			ClipContext(
				this.eyePosition,
				this.eyePosition.add(this.lookAngle.scale(range.toDouble())),
				ClipContext.Block.OUTLINE,
				ClipContext.Fluid.NONE,
				this
			)
		)
	}
}

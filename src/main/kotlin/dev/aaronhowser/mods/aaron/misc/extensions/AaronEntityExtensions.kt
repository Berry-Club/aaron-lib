package dev.aaronhowser.mods.aaron.misc.extensions

import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult

object AaronEntityExtensions {

	val Entity.isClientSide: Boolean get() = this.level().isClientSide
	val Entity.isServerSide: Boolean get() = this.level().isServerSide

	fun LivingEntity.status(message: Component) {
		if (this is Player) this.sendOverlayMessage(message)
	}

	fun LivingEntity.status(message: String) = this.status(Component.literal(message))

	fun LivingEntity.tell(message: Component) {
		if (this is Player) sendSystemMessage(message)
	}

	fun LivingEntity.tell(message: String) = tell(Component.literal(message))

	fun Entity.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.`is`(tagKey)

	fun Entity.isMovingHorizontally(): Boolean {
		return this.deltaMovement.horizontalDistance() > 0.015
	}

	fun Player.giveOrDropStack(itemStack: ItemStack): Boolean {
		if (this.inventory.add(itemStack)) return true

		val entity = ItemEntity(level(), this.x, this.y, this.z, itemStack)
		entity.setNoPickUpDelay()
		return this.level().addFreshEntity(entity)
	}

	//	fun Entity.getMinimalTag(stripUniqueness: Boolean = true): CompoundTag {
//		val nbt = CompoundTag()
//		this.save(nbt)
//		AaronUtil.cleanEntityNbt(nbt, stripUniqueness)
//		return nbt
//	}

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
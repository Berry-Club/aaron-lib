package dev.aaronhowser.mods.aaron.misc

import com.mojang.datafixers.util.Either
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.data.tags.TagAppender
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredBlock

@Suppress("unused")
object RegistryExtensions {
	fun DyeColor.getDyeName(): String = this.getName()
	fun Direction.getDirectionName(): String = this.getName()

	fun <T : Any> Holder<T>.isHolder(location: Identifier): Boolean = this.`is`(location)
	fun <T : Any> Holder<T>.isHolder(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T : Any> Holder<T>.isHolder(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)
	fun <T : Any> Holder<T>.isHolder(holder: Holder<T>): Boolean = this.`is`(holder)

	fun BlockBehaviour.BlockStateBase.isBlock(block: Block): Boolean = this.`is`(block)
	fun BlockBehaviour.BlockStateBase.isBlock(blockHolder: Holder<Block>): Boolean = this.`is`(blockHolder)
	fun BlockBehaviour.BlockStateBase.isBlock(resourceKey: ResourceKey<Block>): Boolean = this.`is`(resourceKey)
	fun BlockBehaviour.BlockStateBase.isBlock(tagKey: TagKey<Block>): Boolean = this.`is`(tagKey)

	fun FluidStack.isFluid(fluid: Fluid): Boolean = this.`is`(fluid)
	fun FluidStack.isFluid(tagKey: TagKey<Fluid>): Boolean = this.`is`(tagKey)
	fun FluidStack.isFluid(fluids: HolderSet<Fluid>): Boolean = this.`is`(fluids)

	fun FluidState.isFluid(fluid: Fluid): Boolean = this.`is`(fluid)
	fun FluidState.isFluid(tagKey: TagKey<Fluid>): Boolean = this.`is`(tagKey)
	fun FluidState.isFluid(fluids: HolderSet<Fluid>): Boolean = this.`is`(fluids)

	fun DamageSource.isDamageSource(tagKey: TagKey<DamageType>): Boolean = this.`is`(tagKey)
	fun DamageSource.isDamageSource(resourceKey: ResourceKey<DamageType>): Boolean = this.`is`(resourceKey)

	fun EntityType<*>.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.builtInRegistryHolder().`is`(tagKey)
	fun Entity.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.type.builtInRegistryHolder().`is`(tagKey)

	fun DeferredBlock<*>.defaultBlockState(): BlockState = this.get().defaultBlockState()

	fun Either<*, *>.isLeft(): Boolean = this.left().isPresent
	fun Either<*, *>.isRight(): Boolean = this.right().isPresent

	fun <T : Any> TagAppender<T, T>.add(vararg holders: Holder<T>): TagAppender<T, T> {
		for (holder in holders) this.add(holder.value())
		return this
	}
}

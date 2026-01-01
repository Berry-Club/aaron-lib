package dev.aaronhowser.mods.aaron

import com.mojang.datafixers.util.Either
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.Vec3i
import net.minecraft.core.component.DataComponentType
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.*

object AaronExtensions {

	val Level.isServerSide: Boolean get() = !this.isClientSide

	val Entity.isClientSide: Boolean get() = this.level().isClientSide
	val Entity.isServerSide: Boolean get() = this.level().isServerSide

	fun Player.status(message: Component) = this.displayClientMessage(message, true)
	fun Player.status(message: String) = this.status(Component.literal(message))

	fun LivingEntity.tell(message: Component) = this.sendSystemMessage(message)
	fun LivingEntity.tell(message: String) = this.tell(Component.literal(message))

	fun Boolean?.isTrue(): Boolean = this == true
	fun Boolean?.isNotTrue(): Boolean = this != true

	fun ItemStack.isNotEmpty(): Boolean = !this.isEmpty
	fun DyeColor.getDyeName(): String = this.getName()
	fun Direction.getDirectionName(): String = this.getName()

	fun ItemStack.isItem(item: Holder<Item>): Boolean = this.`is`(item)
	fun ItemStack.isItem(item: Item): Boolean = this.`is`(item)
	fun ItemStack.isItem(tag: TagKey<Item>): Boolean = this.`is`(tag)

	fun <T> Holder<T>.isHolder(location: ResourceLocation): Boolean = this.`is`(location)
	fun <T> Holder<T>.isHolder(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T> Holder<T>.isHolder(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)

	fun BlockBehaviour.BlockStateBase.isBlock(block: Block): Boolean = this.`is`(block)
	fun BlockBehaviour.BlockStateBase.isBlock(blockHolder: Holder<Block>): Boolean = this.`is`(blockHolder)
	fun BlockBehaviour.BlockStateBase.isBlock(resourceKey: ResourceKey<Block>): Boolean = this.`is`(resourceKey)
	fun BlockBehaviour.BlockStateBase.isBlock(tagKey: TagKey<Block>): Boolean = this.`is`(tagKey)

	fun DamageSource.isDamageSource(tagKey: TagKey<DamageType>): Boolean = this.`is`(tagKey)
	fun DamageSource.isDamageSource(resourceKey: ResourceKey<DamageType>): Boolean = this.`is`(resourceKey)

	fun EntityType<*>.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.`is`(tagKey)
	fun Entity.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.type.`is`(tagKey)

	fun ItemLike.asIngredient(): Ingredient = Ingredient.of(this)
	fun TagKey<Item>.asIngredient(): Ingredient = Ingredient.of(this)

	fun Entity.isMovingHorizontally(): Boolean {
		return this.deltaMovement.horizontalDistance() > 0.015
	}

	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun RandomSource.chance(chance: Number): Boolean = nextDouble() <= chance.toDouble()

	fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
	fun Number.toRadians(): Double = Math.toRadians(this.toDouble())

	fun ItemLike.getDefaultInstance(): ItemStack = this.asItem().defaultInstance

	fun <T> ItemLike.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.set(componentType, component)
		return stack
	}

	fun <T> ItemStack.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		this.set(componentType, component)
		return this
	}

	fun Player.giveOrDropStack(itemStack: ItemStack): Boolean {
		if (this.inventory.add(itemStack)) return true

		val entity = ItemEntity(level(), this.x, this.y, this.z, itemStack)
		entity.setNoPickUpDelay()
		return this.level().addFreshEntity(entity)
	}

	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		return if (this.hasUUID(key)) this.getUUID(key) else null
	}

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> Any?.cast(): T = this as T

	fun Style.withHoverText(component: Component): Style = withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, component))
	fun Style.withHoverText(text: String): Style = withHoverText(Component.literal(text))
	fun Style.withClickToRunCommand(command: String): Style = withClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
	fun Style.withClickToSuggestCommand(command: String): Style = withClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command))
	fun Style.withClickToOpenUrl(url: String): Style = withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, url))
	fun Style.withClickToCopyToClipboard(text: String): Style = withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text))

	fun DeferredBlock<*>.defaultBlockState(): BlockState = this.get().defaultBlockState()

	fun Either<*, *>.isLeft(): Boolean = this.left().isPresent
	fun Either<*, *>.isRight(): Boolean = this.right().isPresent

	fun Entity.getMinimalTag(stripUniqueness: Boolean = true): CompoundTag {
		val nbt = CompoundTag()
		this.save(nbt)
		AaronUtil.cleanEntityNbt(nbt, stripUniqueness)
		return nbt
	}

	fun List<ItemStack>.totalCount(): Int {
		var total = 0
		for (stack in this) {
			total += stack.count
		}
		return total
	}

}
package dev.aaronhowser.mods.aaron.misc

import com.mojang.datafixers.util.Either
import net.minecraft.ChatFormatting
import net.minecraft.core.*
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.tags.TagAppender
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.*
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.util.ProblemReporter
import net.minecraft.util.RandomSource
import net.minecraft.util.Unit
import net.minecraft.world.ContainerHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import org.joml.Vector3f
import java.net.URI
import java.util.*
import java.util.function.Predicate
import java.util.function.Supplier
import kotlin.math.pow

@Suppress("unused")
object AaronExtensions {

	// General

	@Suppress("UNCHECKED_CAST")
	fun <T> Any?.cast(): T = this as T

	fun Boolean?.isNotTrue(): Boolean = this != true
	fun Boolean?.isTrue(): Boolean = this == true

	fun Either<*, *>.isLeft(): Boolean = this.left().isPresent
	fun Either<*, *>.isRight(): Boolean = this.right().isPresent

	// Text and chat

	fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args.map { it ?: "null" }.toTypedArray())
	fun String.toGrayComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args.map { it ?: "null" }.toTypedArray()).withStyle(ChatFormatting.GRAY)

	fun Style.withClickToCopyToClipboard(text: String): Style = withClickEvent(ClickEvent.CopyToClipboard(text))
	fun Style.withClickToOpenUrl(url: String): Style = withClickEvent(ClickEvent.OpenUrl(URI.create(url)))
	fun Style.withClickToRunCommand(command: String): Style = withClickEvent(ClickEvent.RunCommand(command))
	fun Style.withClickToSuggestCommand(command: String): Style = withClickEvent(ClickEvent.SuggestCommand(command))
	fun Style.withHoverText(component: Component): Style = withHoverEvent(HoverEvent.ShowText(component))
	fun Style.withHoverText(text: String): Style = withHoverText(Component.literal(text))

	// Items, blocks, and fluids

	fun ItemLike.asIngredient(): Ingredient = Ingredient.of(this)
	fun ItemLike.getDefaultInstance(): ItemStack = this.asItem().defaultInstance

	fun <T : Any> ItemLike.asIngredient(
		componentType: DataComponentType<in T>,
		component: T,
		strict: Boolean = false
	): Ingredient {
		return DataComponentIngredient.of(strict, componentType, component, this)
	}

	fun <T : Any> ItemLike.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.set(componentType, component)
		return stack
	}

	fun ItemLike.withCount(count: Int): ItemStack {
		val stack = getDefaultInstance()
		stack.count = count
		return stack
	}

	fun ItemLike.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.remove(componentType)
		return stack
	}

	fun ItemStack.asIngredient(strict: Boolean = false): Ingredient {
		return if (isComponentsPatchEmpty) {
			Ingredient.of(this.item)
		} else {
			DataComponentIngredient.of(strict, this)
		}
	}

	fun ItemStack.hasEnchantment(enchantment: Holder<Enchantment>): Boolean = this.getEnchantmentLevel(enchantment) > 0
	fun ItemStack.isFull(): Boolean = this.count >= this.maxStackSize
	fun ItemStack.isItem(item: Holder<Item>): Boolean = this.`is`(item)
	fun ItemStack.isItem(item: Item): Boolean = this.`is`(item)
	fun ItemStack.isItem(tag: TagKey<Item>): Boolean = this.`is`(tag)
	fun ItemStack.isNotEmpty(): Boolean = !this.isEmpty
	fun ItemStack.isNotFull(): Boolean = this.count < this.maxStackSize

	fun ItemStack.setUnit(dataComponent: DataComponentType<Unit>) = this.set(dataComponent, Unit.INSTANCE)
	fun ItemStack.setUnit(dataComponent: Supplier<out DataComponentType<Unit>>) = setUnit(dataComponent.get())

	fun ItemStack.toggleUnit(dataComponent: DataComponentType<Unit>) {
		if (this.has(dataComponent)) {
			this.remove(dataComponent)
		} else {
			this.setUnit(dataComponent)
		}
	}

	fun ItemStack.toggleUnit(dataComponent: Supplier<out DataComponentType<Unit>>) = toggleUnit(dataComponent.get())

	fun <T : Any> ItemStack.withComponent(componentType: DataComponentType<T>, component: T): ItemStack {
		this.set(componentType, component)
		return this
	}

	fun ItemStack.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		this.remove(componentType)
		return this
	}

	fun List<ItemStack>.totalCount(): Int {
		var total = 0
		for (stack in this) {
			total += stack.count
		}
		return total
	}

	fun Holder<Potion>.getAsStack(): ItemStack {
		return PotionContents.createItemStack(Items.POTION, this)
	}

	fun TagKey<Item>.asIngredient(): Ingredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(this))

	fun BlockBehaviour.BlockStateBase.isBlock(block: Block): Boolean = this.`is`(block)
	fun BlockBehaviour.BlockStateBase.isBlock(blockHolder: Holder<Block>): Boolean = this.`is`(blockHolder)
	fun BlockBehaviour.BlockStateBase.isBlock(resourceKey: ResourceKey<Block>): Boolean = this.`is`(resourceKey)
	fun BlockBehaviour.BlockStateBase.isBlock(tagKey: TagKey<Block>): Boolean = this.`is`(tagKey)

	fun DeferredBlock<*>.defaultBlockState(): BlockState = this.get().defaultBlockState()

	fun FluidStack.isFluid(fluid: Fluid): Boolean = this.`is`(fluid)
	fun FluidStack.isFluid(fluids: HolderSet<Fluid>): Boolean = this.`is`(fluids)
	fun FluidStack.isFluid(tagKey: TagKey<Fluid>): Boolean = this.`is`(tagKey)

	fun FluidState.isFluid(fluid: Fluid): Boolean = this.`is`(fluid)
	fun FluidState.isFluid(fluids: HolderSet<Fluid>): Boolean = this.`is`(fluids)
	fun FluidState.isFluid(tagKey: TagKey<Fluid>): Boolean = this.`is`(tagKey)

	// Registries, tags, and resource holders

	fun Direction.getDirectionName(): String = this.getName()
	fun DyeColor.getDyeName(): String = this.getName()

	fun <T : Any> Holder<T>.isHolder(holder: Holder<T>): Boolean = this.`is`(holder)
	fun <T : Any> Holder<T>.isHolder(location: Identifier): Boolean = this.`is`(location)
	fun <T : Any> Holder<T>.isHolder(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T : Any> Holder<T>.isHolder(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)

	fun <T : Any> TagAppender<T, T>.add(vararg holders: Holder<T>): TagAppender<T, T> {
		for (holder in holders) this.add(holder.value())
		return this
	}

	// Players, entities, and levels

	val Level.isServerSide: Boolean get() = !this.isClientSide

	val Entity.isClientSide: Boolean get() = this.level().isClientSide
	val Entity.isServerSide: Boolean get() = this.level().isServerSide

	fun Entity.getMinimalTag(stripUniqueness: Boolean = true): CompoundTag {
		val output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.registryAccess())
		this.save(output)
		val nbt = output.buildResult()
		AaronUtil.cleanEntityNbt(nbt, stripUniqueness)
		return nbt
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

	fun Entity.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.type.builtInRegistryHolder().`is`(tagKey)

	fun Entity.isMovingHorizontally(): Boolean {
		return this.deltaMovement.horizontalDistance() > 0.015
	}

	fun EntityType<*>.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.builtInRegistryHolder().`is`(tagKey)

	fun DamageSource.isDamageSource(resourceKey: ResourceKey<DamageType>): Boolean = this.`is`(resourceKey)
	fun DamageSource.isDamageSource(tagKey: TagKey<DamageType>): Boolean = this.`is`(tagKey)

	fun LivingEntity.tell(message: Component) {
		if (this is Player) this.sendSystemMessage(message)
	}

	fun LivingEntity.tell(message: String) = this.tell(Component.literal(message))

	fun Player.allItemStacksSequence(): Sequence<ItemStack> {
		return inventory.nonEquipmentItems.asSequence() + inventory.equipment.items.values.asSequence()
	}

	fun Player.allItemStacks(): List<ItemStack> = allItemStacksSequence().toList()
	fun Player.getFirstItemStack(predicate: Predicate<ItemStack>): ItemStack? = allItemStacksSequence().firstOrNull(predicate::test)
	fun Player.hasItem(predicate: Predicate<ItemStack>): Boolean = allItemStacksSequence().any(predicate::test)

	fun Player.getPovResult(range: Number = this.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)): BlockHitResult {
		val asEntity = this as Entity
		return asEntity.getPovResult(range)
	}

	fun Player.giveOrDropStack(itemStack: ItemStack): Boolean {
		if (this.inventory.add(itemStack)) return true

		val entity = ItemEntity(level(), this.x, this.y, this.z, itemStack)
		entity.setNoPickUpDelay()
		return this.level().addFreshEntity(entity)
	}

	fun Player.status(message: Component) = this.sendOverlayMessage(message)
	fun Player.status(message: String) = this.status(Component.literal(message))

	// Positions, geometry, and random

	fun BlockPos.closerThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) < distance.toDouble().pow(2)
	fun BlockPos.furtherThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) > distance.toDouble().pow(2)

	fun Vec3.closerThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) < distance.toDouble().pow(2)
	fun Vec3.furtherThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) > distance.toDouble().pow(2)

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Vector3f.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Long.toBlockPos(): BlockPos = BlockPos.of(this)
	fun Long.toChunkPos(): ChunkPos = ChunkPos(this.toInt(), (this shr 32).toInt())

	fun AABB.randomPos(random: RandomSource): Vec3 {
		return Vec3(randomX(random), randomY(random), randomZ(random))
	}

	fun AABB.randomX(random: RandomSource): Double = minX + (xsize * random.nextDouble())
	fun AABB.randomY(random: RandomSource): Double = minY + (ysize * random.nextDouble())
	fun AABB.randomZ(random: RandomSource): Double = minZ + (zsize * random.nextDouble())

	fun RandomSource.chance(chance: Number): Boolean = nextDouble() <= chance.toDouble()
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun RandomSource.oneIn(sides: Int): Boolean = nextInt(0, sides) == 0
	fun RandomSource.roll(chance: Number): Boolean = nextDouble() <= chance.toDouble()

	fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
	fun Number.toRadians(): Double = Math.toRadians(this.toDouble())

	// Colors

	fun Int.toArgb(): ARGB = ARGB.fromInt(this)
	fun Int.toRgb(): RGB = RGB.fromInt(this)
	fun Int.toRgba(): RGBA = RGBA.fromInt(this)

	// NBT, inventory storage, and energy storage

	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		return this.getIntArray(key)
			.filter { it.size == 4 }
			.map(UUIDUtil::uuidFromIntArray)
			.orElse(null)
	}

	fun CompoundTag.putUuidIfNotNull(key: String, uuid: UUID?): CompoundTag {
		if (uuid != null) this.putIntArray(key, UUIDUtil.uuidToIntArray(uuid))
		return this
	}

	fun ValueInput.loadEnergy(name: String, energyStorage: SimpleEnergyHandler) {
		this.child(name).ifPresent(energyStorage::deserialize)
	}

	fun ValueInput.loadItems(container: SimpleContainer) {
		loadItems(container.items)
	}

	fun ValueInput.loadItems(items: NonNullList<ItemStack>) {
		ContainerHelper.loadAllItems(this, items)
	}

	fun ValueOutput.saveEnergy(name: String, energyStorage: SimpleEnergyHandler) {
		energyStorage.serialize(this.child(name))
	}

	fun ValueOutput.saveItems(container: SimpleContainer) {
		saveItems(container.items)
	}

	fun ValueOutput.saveItems(items: NonNullList<ItemStack>) {
		ContainerHelper.saveAllItems(this, items)
	}

	// Collections

	fun <T> Collection<T>.random(random: RandomSource): T {
		if (isEmpty()) throw NoSuchElementException("Collection is empty.")
		return elementAt(random.nextInt(size))
	}

	fun <T> Collection<T>.randomOrNull(random: RandomSource): T? {
		if (isEmpty()) return null
		return elementAt(random.nextInt(size))
	}

	fun <T> MutableList<T>.shuffle(random: RandomSource) {
		for (i in lastIndex downTo 1) {
			val j = random.nextInt(i + 1)
			val temp = this[i]
			this[i] = this[j]
			this[j] = temp
		}
	}

}

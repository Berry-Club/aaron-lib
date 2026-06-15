package dev.aaronhowser.mods.aaron.misc

import com.mojang.datafixers.util.Either
import net.minecraft.ChatFormatting
import net.minecraft.core.*
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.core.component.DataComponentType
import net.minecraft.data.tags.IntrinsicHolderTagsProvider
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.chat.*
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
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
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.DeferredBlock
import org.joml.Vector3f
import java.util.*
import java.util.function.Supplier
import kotlin.math.pow

@Suppress("unused")
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

	fun DyeColor.getDyeName(): String = this.getName()
	fun Direction.getDirectionName(): String = this.getName()

	fun ItemStack.isItem(item: Holder<Item>): Boolean = this.`is`(item)
	fun ItemStack.isItem(item: Item): Boolean = this.`is`(item)
	fun ItemStack.isItem(tag: TagKey<Item>): Boolean = this.`is`(tag)

	fun <T> Holder<T>.isHolder(location: ResourceLocation): Boolean = this.`is`(location)
	fun <T> Holder<T>.isHolder(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T> Holder<T>.isHolder(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)
	fun <T> Holder<T>.isHolder(holder: Holder<T>): Boolean = this.`is`(holder)

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

	fun EntityType<*>.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.`is`(tagKey)
	fun Entity.isEntity(tagKey: TagKey<EntityType<*>>): Boolean = this.type.`is`(tagKey)

	fun ItemLike.asIngredient(): Ingredient = Ingredient.of(this)
	fun TagKey<Item>.asIngredient(): Ingredient = Ingredient.of(this)
	fun ItemStack.asIngredient(strict: Boolean = false): Ingredient {
		return if (isComponentsPatchEmpty) {
			Ingredient.of(this)
		} else {
			DataComponentIngredient.of(strict, this)
		}
	}

	fun ItemLike.asIngredient(
		predicate: DataComponentPredicate,
		strict: Boolean = false
	): Ingredient {
		return DataComponentIngredient.of(strict, predicate, this)
	}

	fun <T> ItemLike.asIngredient(
		componentType: DataComponentType<in T>,
		component: T,
	): Ingredient {
		val predicate = DataComponentPredicate.builder().expect(componentType, component).build()
		return asIngredient(predicate)
	}

	fun Entity.isMovingHorizontally(): Boolean {
		return this.deltaMovement.horizontalDistance() > 0.015
	}

	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun RandomSource.chance(chance: Number): Boolean = nextDouble() <= chance.toDouble()

	fun RandomSource.roll(chance: Number): Boolean = nextDouble() <= chance.toDouble()
	fun RandomSource.oneIn(sides: Int): Boolean = nextInt(0, sides) == 0

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

	fun ItemLike.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		val stack = this.asItem().defaultInstance
		stack.remove(componentType)
		return stack
	}

	fun ItemStack.withoutComponent(componentType: DataComponentType<*>): ItemStack {
		this.remove(componentType)
		return this
	}

	fun ItemLike.withCount(count: Int): ItemStack {
		val stack = getDefaultInstance()
		stack.count = count
		return stack
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

	fun CompoundTag.putUuidIfNotNull(key: String, uuid: UUID?): CompoundTag {
		if (uuid != null) this.putUUID(key, uuid)
		return this
	}

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	fun Vector3f.toVec3(): Vec3 {
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

	fun Holder<Potion>.getAsStack(): ItemStack {
		return PotionContents.createItemStack(Items.POTION, this)
	}

	fun ItemStack.hasEnchantment(enchantment: Holder<Enchantment>): Boolean = this.getEnchantmentLevel(enchantment) > 0
	fun ItemStack.isNotEmpty(): Boolean = !this.isEmpty
	fun ItemStack.isFull(): Boolean = this.count >= this.maxStackSize
	fun ItemStack.isNotFull(): Boolean = this.count < this.maxStackSize

	fun Long.toBlockPos(): BlockPos = BlockPos.of(this)
	fun Long.toChunkPos(): ChunkPos = ChunkPos(this)

	fun <T> IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>.add(vararg holders: Holder<T>): IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> {
		for (holder in holders) this.add(holder.value())
		return this
	}

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

	fun CompoundTag.saveItems(items: NonNullList<ItemStack>, registries: HolderLookup.Provider) {
		ContainerHelper.saveAllItems(this, items, registries)
	}

	fun CompoundTag.saveItems(container: SimpleContainer, registries: HolderLookup.Provider) {
		saveItems(container.items, registries)
	}

	fun CompoundTag.loadItems(items: NonNullList<ItemStack>, registries: HolderLookup.Provider) {
		ContainerHelper.loadAllItems(this, items, registries)
	}

	fun CompoundTag.loadItems(container: SimpleContainer, registries: HolderLookup.Provider) {
		loadItems(container.items, registries)
	}

	fun CompoundTag.saveEnergy(name: String, energyStorage: EnergyStorage, registries: HolderLookup.Provider) {
		this.put(name, energyStorage.serializeNBT(registries))
	}

	fun CompoundTag.loadEnergy(name: String, energyStorage: EnergyStorage, registries: HolderLookup.Provider) {
		val energyTag = this.get(name)
		if (energyTag is IntTag) {
			energyStorage.deserializeNBT(registries, energyTag)
		}
	}

	fun Int.toRgb(): RGB = RGB.fromInt(this)
	fun Int.toArgb(): ARGB = ARGB.fromInt(this)
	fun Int.toRgba(): RGBA = RGBA.fromInt(this)

	fun <T : ModelBuilder<T>> ModelBuilder<T>.particle(location: ResourceLocation): T {
		return texture("particle", location)
	}

	fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)
	fun String.toGrayComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args).withStyle(ChatFormatting.GRAY)

	fun BlockPos.furtherThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) > distance.toDouble().pow(2)
	fun Vec3.furtherThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) > distance.toDouble().pow(2)

	fun BlockPos.closerThan(other: BlockPos, distance: Number): Boolean = this.distSqr(other) < distance.toDouble().pow(2)
	fun Vec3.closerThan(other: Vec3, distance: Number): Boolean = this.distanceToSqr(other) < distance.toDouble().pow(2)

	fun <T> Collection<T>.random(random: RandomSource): T {
		val size = count()
		require(size > 0)

		if (size == 1) return first()

		val index = random.nextInt(size)
		return elementAt(index)
	}

	fun <T> Collection<T>.randomOrNull(random: RandomSource): T? {
		val size = count()
		val index = random.nextInt(size)
		return elementAtOrNull(index)
	}

}
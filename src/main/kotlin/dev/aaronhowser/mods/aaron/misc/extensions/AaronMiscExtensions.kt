package dev.aaronhowser.mods.aaron.misc.extensions

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.datafixers.util.Either
import dev.aaronhowser.mods.aaron.misc.ARGB
import dev.aaronhowser.mods.aaron.misc.RGB
import dev.aaronhowser.mods.aaron.misc.RGBA
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock
import java.net.URI
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Suppress("unused")
object AaronMiscExtensions {

	fun Boolean?.isTrue(): Boolean = this == true
	fun Boolean?.isNotTrue(): Boolean = this != true

	fun DyeColor.getDyeName(): String = this.getName()
	fun Direction.getDirectionName(): String = this.getName()

	fun <T : Any> Holder<T>.isHolder(location: Identifier): Boolean = this.`is`(location)
	fun <T : Any> Holder<T>.isHolder(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T : Any> Holder<T>.isHolder(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)
	fun <T : Any> Holder<T>.isHolder(holder: Holder<T>): Boolean = this.`is`(holder)

	fun <T : Any> TypedInstance<T>.isTag(tagKey: TagKey<T>): Boolean = this.`is`(tagKey)
	fun <T : Any> TypedInstance<T>.isHolderSet(holderSet: HolderSet<T>): Boolean = this.`is`(holderSet)
	fun <T : Any> TypedInstance<T>.isHolder(holder: Holder<T>): Boolean = this.`is`(holder)
	fun <T : Any> TypedInstance<T>.isResourceKey(resourceKey: ResourceKey<T>): Boolean = this.`is`(resourceKey)
	fun <T : Any> TypedInstance<T>.isRawType(rawType: T): Boolean = this.`is`(rawType)

	fun DamageSource.isDamageSource(tagKey: TagKey<DamageType>): Boolean = this.`is`(tagKey)
	fun DamageSource.isDamageSource(resourceKey: ResourceKey<DamageType>): Boolean = this.`is`(resourceKey)

	fun RandomSource.nextRange(min: Float, max: Float): Float = Mth.lerp(nextFloat(), min, max)
	fun RandomSource.nextRange(min: Double, max: Double): Double = Mth.lerp(nextDouble(), min, max)
	fun RandomSource.nextRange(min: Int, max: Int): Int = nextInt(max - min) + min
	fun RandomSource.chance(chance: Number): Boolean = nextDouble() <= chance.toDouble()

	fun Number.toDegrees(): Double = Math.toDegrees(this.toDouble())
	fun Number.toRadians(): Double = Math.toRadians(this.toDouble())

	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		val intArray = getIntArray(key).getOrNull() ?: return null
		if (intArray.size != 4) return null
		return UUIDUtil.uuidFromIntArray(intArray)
	}

	fun CompoundTag.putUuidIfNotNull(key: String, uuid: UUID?): CompoundTag {
		if (uuid == null) {
			return this
		}

		putIntArray(key, UUIDUtil.uuidToIntArray(uuid))
		return this
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> Any?.cast(): T = this as T

	fun Style.withHoverText(component: Component): Style = withHoverEvent(HoverEvent.ShowText(component))
	fun Style.withHoverText(text: String): Style = withHoverText(Component.literal(text))
	fun Style.withClickToRunCommand(command: String): Style = withClickEvent(ClickEvent.RunCommand(command))
	fun Style.withClickToSuggestCommand(command: String): Style = withClickEvent(ClickEvent.SuggestCommand(command))
	fun Style.withClickToCopyToClipboard(text: String): Style = withClickEvent(ClickEvent.CopyToClipboard(text))
	fun Style.withClickToOpenUrl(uri: URI): Style = withClickEvent(ClickEvent.OpenUrl(uri))
	fun Style.withClickToOpenUrl(uri: String): Style = withClickToOpenUrl(URI(uri))

	fun DeferredBlock<*>.defaultBlockState(): BlockState = this.get().defaultBlockState()

	fun Either<*, *>.isLeft(): Boolean = this.left().isPresent
	fun Either<*, *>.isRight(): Boolean = this.right().isPresent

//	fun <T> IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>.add(vararg holders: Holder<T>): IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> {
//		for (holder in holders) this.add(holder.value())
//		return this
//	}

//	fun CompoundTag.saveItems(items: NonNullList<ItemStack>, registries: HolderLookup.Provider) {
//		ContainerHelper.saveAllItems(this, items, registries)
//	}

//	fun CompoundTag.saveItems(container: SimpleContainer, registries: HolderLookup.Provider) {
//		saveItems(container.items, registries)
//	}

//	fun CompoundTag.loadItems(items: NonNullList<ItemStack>, registries: HolderLookup.Provider) {
//		ContainerHelper.loadAllItems(this, items, registries)
//	}
//
//	fun CompoundTag.loadItems(container: SimpleContainer, registries: HolderLookup.Provider) {
//		loadItems(container.items, registries)
//	}

//	fun CompoundTag.saveEnergy(name: String, energyStorage: EnergyStorage, registries: HolderLookup.Provider) {
//		this.put(name, energyStorage.serializeNBT(registries))
//	}

//	fun CompoundTag.loadEnergy(name: String, energyStorage: EnergyStorage, registries: HolderLookup.Provider) {
//		val energyTag = this.get(name)
//		if (energyTag is IntTag) {
//			energyStorage.deserializeNBT(registries, energyTag)
//		}
//	}

	fun Int.toRgb(): RGB = RGB.fromInt(this)
	fun Int.toArgb(): ARGB = ARGB.fromInt(this)
	fun Int.toRgba(): RGBA = RGBA.fromInt(this)

//	fun <T : ModelBuilder<T>> ModelBuilder<T>.particle(location: Identifier): T {
//		return texture("particle", location)
//	}

	fun <S : CommandSourceStack, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.requiresModerator(): T {
		return this.requires(Commands.hasPermission(Commands.LEVEL_MODERATORS))
	}

	fun <S : CommandSourceStack, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.requiresGameMaster(): T {
		return this.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
	}

	fun <S : CommandSourceStack, T : ArgumentBuilder<S, T>> ArgumentBuilder<S, T>.requiresAdmin(): T {
		return this.requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
	}

}
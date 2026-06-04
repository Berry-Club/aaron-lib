package dev.aaronhowser.mods.aaron.misc

import dev.aaronhowser.mods.aaron.config.ServerConfig
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.UsernameCache
import java.util.*
import kotlin.math.sqrt

object AaronUtil {

	fun getCachedUuid(playerUsername: String): UUID? {
		val map = UsernameCache.getMap()
		for ((uuid, username) in map) {
			if (username.equals(playerUsername, ignoreCase = true)) {
				return uuid
			}
		}

		return null
	}

	fun cleanEntityNbt(compoundTag: CompoundTag, stripUniqueness: Boolean = false) {
		val removeKeys = buildList {
			addAll(ServerConfig.CONFIG.cleanEntityNbtKeys.get())

			if (stripUniqueness) {
				addAll(ServerConfig.CONFIG.cleanEntityNbtKeysStripUniqueness.get())
			}
		}

		for (tag in removeKeys) {
			compoundTag.remove(tag)
		}
	}

	fun flattenStacks(input: List<ItemStack>): List<ItemStack> {
		val output = mutableListOf<ItemStack>()
		val inputCopy = input.filterNot(ItemStack::isEmpty).map(ItemStack::copy)

		for (stack in inputCopy) {
			if (stack.isEmpty) continue

			val matchingStack = output.firstOrNull { ItemStack.isSameItemSameComponents(it, stack) }

			if (matchingStack != null) {
				while (!stack.isEmpty && matchingStack.count < matchingStack.maxStackSize) {
					stack.shrink(1)
					matchingStack.grow(1)
				}
			}

			if (!stack.isEmpty) {
				output.add(stack)
			}
		}

		return output
	}

	fun dropStackAt(itemStack: ItemStack, entity: Entity, instantPickup: Boolean = false): Boolean {
		return dropStackAt(itemStack, entity.level(), entity.position(), instantPickup)
	}

	fun dropStackAt(itemStack: ItemStack, level: Level, pos: Vec3, instantPickup: Boolean = false): Boolean {
		val itemEntity = ItemEntity(level, pos.x, pos.y, pos.z, itemStack)
		if (instantPickup) itemEntity.setNoPickUpDelay()
		return level.addFreshEntity(itemEntity)
	}

	fun getGridSpiralPos(index: Int): Pair<Int, Int> {
		if (index == 0) return 0 to 0

		// The ring's distance from the center
		val ring = Mth.ceil((sqrt(index + 1.0) - 1) / 2.0)

		// The index of the first element in the ring
		val ringStart = (2 * ring - 1) * (2 * ring - 1)

		val sideLength = 2 * ring
		val posInRing = index - ringStart                        // How far into this ring the index is
		val side = posInRing / sideLength                        // Which of the 4 sides of the ring the index is on
		val offset = posInRing % sideLength - ring                // How far along the side the index is

		return when (side) {
			0 -> ring - posInRing to -ring
			1 -> -ring to -ring + offset
			2 -> -ring + offset to ring
			else -> ring to ring - offset
		}
	}

}
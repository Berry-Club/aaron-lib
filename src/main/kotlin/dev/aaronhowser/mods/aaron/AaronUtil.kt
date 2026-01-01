package dev.aaronhowser.mods.aaron

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.UsernameCache
import java.util.*

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
		val badTags = buildList {
			addAll(
				listOf(
					"HurtByTimestamp",
					"Sitting",
					"FallFlying",
					"PortalCooldown",
					"FallDistance",
					"InLove",
					"DeathTime",
					"ForcedAge",
					"Motion",
					"Air",
					"OnGround",
					"Rotation",
					"Pos",
					"HurtTime"
				)
			)

			if (stripUniqueness) {
				add("id")
				add("UUID")
				add("Owner")
				add("Age")
			}
		}

		for (tag in badTags) {
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

}
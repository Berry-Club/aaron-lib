package dev.aaronhowser.mods.aaron.misc

import net.minecraft.util.RandomSource

@Suppress("unused")
object CollectionExtensions {
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

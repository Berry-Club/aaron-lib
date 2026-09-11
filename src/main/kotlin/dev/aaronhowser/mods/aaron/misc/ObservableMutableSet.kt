package dev.aaronhowser.mods.aaron.misc

import java.util.function.Consumer

class ObservableMutableSet<E>(
	private val backingSet: MutableSet<E>,
	private val onChange: Consumer<Set<E>>
) : MutableSet<E> {

	private fun notifyChange() = onChange.accept(backingSet)

	override val size: Int get() = backingSet.size

	override fun contains(element: E): Boolean = backingSet.contains(element)
	override fun containsAll(elements: Collection<E>): Boolean = backingSet.containsAll(elements)
	override fun isEmpty(): Boolean = backingSet.isEmpty()

	override fun add(element: E): Boolean {
		val added = backingSet.add(element)
		if (added) notifyChange()
		return added
	}

	override fun addAll(elements: Collection<E>): Boolean {
		val changed = backingSet.addAll(elements)
		if (changed) notifyChange()
		return changed
	}

	override fun clear() {
		if (backingSet.isNotEmpty()) {
			backingSet.clear()
			notifyChange()
		}
	}

	override fun remove(element: E): Boolean {
		val changed = backingSet.remove(element)
		if (changed) notifyChange()
		return changed
	}

	@Suppress("ConvertArgumentToSet")
	override fun removeAll(elements: Collection<E>): Boolean {
		val changed = backingSet.removeAll(elements)
		if (changed) notifyChange()
		return changed
	}

	@Suppress("ConvertArgumentToSet")
	override fun retainAll(elements: Collection<E>): Boolean {
		val changed = backingSet.retainAll(elements)
		if (changed) notifyChange()
		return changed
	}

	override fun iterator(): MutableIterator<E> {
		return object : MutableIterator<E> {
			private val backingIterator = backingSet.iterator()

			override fun hasNext(): Boolean = backingIterator.hasNext()
			override fun next(): E = backingIterator.next()
			override fun remove() {
				backingIterator.remove()
				notifyChange()
			}
		}
	}

}
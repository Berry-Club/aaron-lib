package dev.aaronhowser.mods.aaron.misc

import java.util.*

fun <T : Any> weakMutableSet(): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>())

fun <T> observableMutableSetOf(vararg elements: T, onChange: (Set<T>) -> Unit): ObservableMutableSet<T> {
	return ObservableMutableSet(mutableSetOf(*elements), onChange)
}

fun <T> observableMutableSetOf(onChange: (Set<T>) -> Unit): ObservableMutableSet<T> {
	return ObservableMutableSet(mutableSetOf(), onChange)
}
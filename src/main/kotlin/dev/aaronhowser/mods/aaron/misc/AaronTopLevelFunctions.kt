package dev.aaronhowser.mods.aaron.misc

import java.util.*

fun <T : Any> weakMutableSet(): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>())
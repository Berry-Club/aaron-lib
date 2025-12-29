package dev.aaronhowser.mods.aaron

import java.util.*

fun <T : Any> weakMutableSet(): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>())
package dev.aaronhowser.mods.aaron.misc

@Suppress("unused")
object CastExtensions {
	@Suppress("UNCHECKED_CAST")
	fun <T> Any?.cast(): T = this as T
}

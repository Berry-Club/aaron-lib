package dev.aaronhowser.mods.aaron.misc

@Suppress("unused")
object BooleanExtensions {
	fun Boolean?.isTrue(): Boolean = this == true
	fun Boolean?.isNotTrue(): Boolean = this != true
}

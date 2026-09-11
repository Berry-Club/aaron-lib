package dev.aaronhowser.mods.aaron.misc

data class RGB(
	val red: Int,
	val green: Int,
	val blue: Int
) {

	fun toInt(): Int {
		return (red shl 16) or (green shl 8) or blue
	}

	companion object {
		fun fromInt(rgb: Int): RGB {
			val red = (rgb shr 16) and 0xFF
			val green = (rgb shr 8) and 0xFF
			val blue = rgb and 0xFF
			return RGB(red, green, blue)
		}
	}
}

data class ARGB(
	val alpha: Int,
	val red: Int,
	val green: Int,
	val blue: Int
) {

	fun toInt(): Int {
		return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
	}

	companion object {
		fun fromInt(argb: Int): ARGB {
			val alpha = (argb shr 24) and 0xFF
			val red = (argb shr 16) and 0xFF
			val green = (argb shr 8) and 0xFF
			val blue = argb and 0xFF
			return ARGB(alpha, red, green, blue)
		}
	}
}

data class RGBA(
	val red: Int,
	val green: Int,
	val blue: Int,
	val alpha: Int
) {

	fun toInt(): Int {
		return (red shl 24) or (green shl 16) or (blue shl 8) or alpha
	}

	companion object {
		fun fromInt(rgba: Int): RGBA {
			val red = (rgba shr 24) and 0xFF
			val green = (rgba shr 16) and 0xFF
			val blue = (rgba shr 8) and 0xFF
			val alpha = rgba and 0xFF
			return RGBA(red, green, blue, alpha)
		}
	}
}
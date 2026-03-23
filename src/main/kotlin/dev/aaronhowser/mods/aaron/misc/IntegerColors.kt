package dev.aaronhowser.mods.aaron.misc

data class RGB(
	val red: Int,
	val green: Int,
	val blue: Int
) {
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
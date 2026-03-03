package dev.aaronhowser.mods.aaron.misc

import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.common.ModConfigSpec

object AaronDsls {

	inline fun ModConfigSpec.Builder.section(name: String, block: ModConfigSpec.Builder.() -> Unit) {
		push(name)
		block()
		pop()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.element(block: ModelBuilder<T>.ElementBuilder.() -> Unit): T {
		val elementBuilder = this.element()
		elementBuilder.block()
		return elementBuilder.end()
	}

}
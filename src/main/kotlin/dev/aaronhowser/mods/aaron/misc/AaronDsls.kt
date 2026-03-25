package dev.aaronhowser.mods.aaron.misc

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.common.ModConfigSpec

object AaronDsls {

	inline fun ModConfigSpec.Builder.section(
		name: String,
		block: () -> Unit
	) {
		push(name)
		block()
		pop()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.element(
		block: ModelBuilder<T>.ElementBuilder.() -> Unit
	): T {
		val elementBuilder = this.element()
		elementBuilder.block()
		return elementBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.ElementBuilder.face(
		direction: Direction,
		block: ModelBuilder<T>.ElementBuilder.FaceBuilder.() -> Unit
	): ModelBuilder<T>.ElementBuilder {
		val faceBuilder = this.face(direction)
		faceBuilder.block()
		return faceBuilder.end()
	}

	inline fun ItemModelBuilder.override(
		block: ItemModelBuilder.OverrideBuilder.() -> Unit
	): ItemModelBuilder {
		val overrideBuilder = this.override()
		overrideBuilder.block()
		return overrideBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.transforms(block: ModelBuilder<T>.TransformsBuilder.() -> Unit): T {
		val transformsBuilder = this.transforms()
		transformsBuilder.block()
		return transformsBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.TransformsBuilder.transform(
		type: ItemDisplayContext,
		block: ModelBuilder<T>.TransformsBuilder.TransformVecBuilder.() -> Unit
	): ModelBuilder<T>.TransformsBuilder {
		val transformVecBuilder = this.transform(type)
		transformVecBuilder.block()
		return transformVecBuilder.end()
	}

	inline fun PoseStack.withPose(block: () -> Unit) {
		pushPose()

		try {
			block()
		} finally {
			popPose()
		}
	}

}
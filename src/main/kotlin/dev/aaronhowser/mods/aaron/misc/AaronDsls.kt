package dev.aaronhowser.mods.aaron.misc

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import net.neoforged.neoforge.common.ModConfigSpec

@DslMarker
@Target(AnnotationTarget.TYPE)
annotation class AaronDslMarker

object AaronDsls {

	inline fun ModConfigSpec.Builder.section(
		name: String,
		block: @AaronDslMarker ModConfigSpec.Builder.() -> Unit
	) {
		push(name)

		try {
			block()
		} finally {
			pop()
		}
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.element(
		block: @AaronDslMarker ModelBuilder<T>.ElementBuilder.() -> Unit
	): T {
		val elementBuilder = this.element()
		elementBuilder.block()
		return elementBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.ElementBuilder.face(
		direction: Direction,
		block: @AaronDslMarker ModelBuilder<T>.ElementBuilder.FaceBuilder.() -> Unit
	): ModelBuilder<T>.ElementBuilder {
		val faceBuilder = this.face(direction)
		faceBuilder.block()
		return faceBuilder.end()
	}

	inline fun ItemModelBuilder.override(
		block: @AaronDslMarker ItemModelBuilder.OverrideBuilder.() -> Unit
	): ItemModelBuilder {
		val overrideBuilder = this.override()
		overrideBuilder.block()
		return overrideBuilder.end()
	}

	inline fun MultiPartBlockStateBuilder.part(
		block: @AaronDslMarker ConfiguredModel.Builder<MultiPartBlockStateBuilder.PartBuilder>.() -> Unit
	): MultiPartBlockStateBuilder.PartBuilder {
		val partBuilder = this.part()
		partBuilder.block()
		return partBuilder.addModel()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.transforms(
		block: @AaronDslMarker ModelBuilder<T>.TransformsBuilder.() -> Unit
	): T {
		val transformsBuilder = this.transforms()
		transformsBuilder.block()
		return transformsBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.TransformsBuilder.transform(
		type: ItemDisplayContext,
		block: @AaronDslMarker ModelBuilder<T>.TransformsBuilder.TransformVecBuilder.() -> Unit
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
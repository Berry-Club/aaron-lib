package dev.aaronhowser.mods.aaron.misc

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ModelBuilder
import net.minecraftforge.common.ForgeConfigSpec

object AaronDsls {

	@DslMarker
	@Target(AnnotationTarget.TYPE)
	annotation class AaronDslMarker

	typealias AaronDslBlock<T> = @AaronDslMarker T.() -> Unit

	inline fun ForgeConfigSpec.Builder.section(
		name: String,
		block: @AaronDslMarker ForgeConfigSpec.Builder.() -> Unit
	) {
		push(name)

		try {
			block()
		} finally {
			pop()
		}
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.element(
		block: AaronDslBlock<ModelBuilder<T>.ElementBuilder>
	): T {
		val elementBuilder = this.element()
		elementBuilder.block()
		return elementBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.ElementBuilder.face(
		direction: Direction,
		block: AaronDslBlock<ModelBuilder<T>.ElementBuilder.FaceBuilder>
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

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.transforms(
		block: AaronDslBlock<ModelBuilder<T>.TransformsBuilder>
	): T {
		val transformsBuilder = this.transforms()
		transformsBuilder.block()
		return transformsBuilder.end()
	}

	inline fun <T : ModelBuilder<T>> ModelBuilder<T>.TransformsBuilder.transform(
		type: ItemDisplayContext,
		block: AaronDslBlock<ModelBuilder<T>.TransformsBuilder.TransformVecBuilder>
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
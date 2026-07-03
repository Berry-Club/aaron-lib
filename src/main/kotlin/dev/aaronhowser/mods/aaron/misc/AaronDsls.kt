package dev.aaronhowser.mods.aaron.misc

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.generators.template.ElementBuilder
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder
import net.neoforged.neoforge.client.model.generators.template.FaceBuilder
import net.neoforged.neoforge.client.model.generators.template.TransformVecBuilder
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

	inline fun ExtendedModelTemplateBuilder.element(
		crossinline block: @AaronDslMarker ElementBuilder.() -> Unit
	): ExtendedModelTemplateBuilder {
		return element { it.block() }
	}

	inline fun ElementBuilder.face(
		direction: Direction,
		crossinline block: @AaronDslMarker FaceBuilder.() -> Unit
	): ElementBuilder {
		return face(direction) { it.block() }
	}

	inline fun ExtendedModelTemplateBuilder.transform(
		type: ItemDisplayContext,
		crossinline block: @AaronDslMarker TransformVecBuilder.() -> Unit
	): ExtendedModelTemplateBuilder {
		return transform(type) { it.block() }
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

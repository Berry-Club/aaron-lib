package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType

object AaronRenderTypes {

	private const val BUFFER_SIZE = 1536

	val LINES_THROUGH_WALLS_PIPELINE: RenderPipeline =
		RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
			.withLocation(AaronLib.modResource("pipeline/lines_through_walls"))
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withDepthWrite(false)
			.build()

	val QUADS_THROUGH_WALLS_PIPELINE: RenderPipeline =
		RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
			.withLocation(AaronLib.modResource("pipeline/quads_through_walls"))
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withDepthWrite(false)
			.withCull(false)
			.build()

	val LINES_THROUGH_WALLS: RenderType.CompositeRenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:lines_through_walls",
			BUFFER_SIZE,
			LINES_THROUGH_WALLS_PIPELINE,
			RenderType.CompositeState.builder()
				.setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
				.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
				.createCompositeState(false)
		)

	val QUADS_THROUGH_WALLS: RenderType.CompositeRenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:quads_through_walls",
			BUFFER_SIZE,
			false,
			true,
			QUADS_THROUGH_WALLS_PIPELINE,
			RenderType.CompositeState.builder()
				.createCompositeState(false)
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALLS
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALLS

}
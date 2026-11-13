package dev.aaronhowser.mods.aaron.client.render

import com.mojang.renderpearl.api.pipeline.CompareOp
import com.mojang.renderpearl.api.pipeline.DepthStencilState
import com.mojang.renderpearl.api.pipeline.RenderPipeline
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

object AaronRenderTypes {

	val LINES_THROUGH_WALLS_PIPELINE: RenderPipeline =
		RenderPipelines.LINES.toBuilder()
			.withLocation("${AaronLib.MOD_ID}:pipeline/lines_through_walls")
			.withDepthStencilState(DepthStencilState(CompareOp.ALWAYS_PASS, false))
			.build()

	val QUADS_THROUGH_WALLS_PIPELINE: RenderPipeline =
		RenderPipelines.DEBUG_QUADS.toBuilder()
			.withLocation("${AaronLib.MOD_ID}:pipeline/quads_through_walls")
			.withDepthStencilState(DepthStencilState(CompareOp.ALWAYS_PASS, false))
			.withCull(false)
			.build()

	val LINES_THROUGH_WALLS: RenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:lines_through_walls",
			RenderSetup.builder(LINES_THROUGH_WALLS_PIPELINE)
				.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
				.createRenderSetup()
		)

	val QUADS_THROUGH_WALLS: RenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:quads_through_walls",
			RenderSetup.builder(QUADS_THROUGH_WALLS_PIPELINE)
				.sortOnUpload()
				.createRenderSetup()
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALLS
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALLS

}
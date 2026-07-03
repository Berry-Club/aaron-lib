package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.platform.CompareOp
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

object AaronRenderTypes {

	private const val BUFFER_SIZE = 1536

	val LINES_THROUGH_WALLS: RenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:lines_through_walls",
			RenderSetup.builder(
				RenderPipelines.LINES.toBuilder()
					.withLocation("${AaronLib.MOD_ID}:pipeline/lines_through_walls")
					.withDepthStencilState(DepthStencilState(CompareOp.ALWAYS_PASS, false))
					.build()
			)
				.bufferSize(BUFFER_SIZE)
				.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
				.createRenderSetup()
		)

	val QUADS_THROUGH_WALLS: RenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:quads_through_walls",
			RenderSetup.builder(
				RenderPipelines.DEBUG_QUADS.toBuilder()
					.withLocation("${AaronLib.MOD_ID}:pipeline/quads_through_walls")
					.withDepthStencilState(DepthStencilState(CompareOp.ALWAYS_PASS, false))
					.withCull(false)
					.build()
			)
				.bufferSize(BUFFER_SIZE)
				.sortOnUpload()
				.createRenderSetup()
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALLS
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALLS

}

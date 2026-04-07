package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderPipelines
import java.util.*

object AaronRenderPipelines {

	val LINES_THROUGH_WALL: RenderPipeline =
		RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
			.withLocation(AaronLib.modResource("line_through_wall"))
			.withCull(false)
			.withDepthStencilState(Optional.empty())
			.build()

//	val QUADS_THROUGH_WALL: RenderPipeline =
//		RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
//			.withLocation(AaronLib.modResource("quads_through_wall"))
//			.withCull(false)
//			.withDepthStencilState(Optional.empty())
//			.build()

}
package dev.aaronhowser.mods.aaron.client.render

import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.OutputTarget
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType

object AaronRenderTypes {

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	val LINES_THROUGH_WALL_RENDER_TYPE =
		RenderType.create(
			"${AaronLib.MOD_ID}:lines_through_wall",
			RenderSetup.builder(AaronRenderPipelines.LINES_THROUGH_WALL)
				.setLayeringTransform(LayeringTransform.NO_LAYERING)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.createRenderSetup()
		)

//	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
//	val QUADS_THROUGH_WALL_RENDER_TYPE: RenderType.CompositeRenderType =
//		RenderType.create(
//			"${AaronLib.MOD_ID}:quads_through_wall",
//			DefaultVertexFormat.POSITION_COLOR,
//			VertexFormat.Mode.QUADS,
//			1536,
//			false,
//			true,
//			RenderType.CompositeState.builder()
//				.setShaderState(POSITION_COLOR_SHADER)
//				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//				.setCullState(NO_CULL)
//				.setDepthTestState(NO_DEPTH_TEST)
//				.setWriteMaskState(COLOR_WRITE)
//				.createCompositeState(false)
//		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALL_RENDER_TYPE
//	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALL_RENDER_TYPE

}
package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import java.util.*

object AaronRenderTypes {

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	val LINES_THROUGH_WALL_RENDER_TYPE: RenderType.CompositeRenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:line_through_wall",
			DefaultVertexFormat.POSITION_COLOR_NORMAL,
			VertexFormat.Mode.LINES,
			1536,
			true,
			false,
			RenderType.CompositeState.builder()
				.setShaderState(RENDERTYPE_LINES_SHADER)
				.setLineState(LineStateShard(OptionalDouble.empty()))
				.setLayeringState(VIEW_OFFSET_Z_LAYERING)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.setCullState(NO_CULL)
				.setDepthTestState(NO_DEPTH_TEST)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false)
		)

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	val QUADS_THROUGH_WALL_RENDER_TYPE: RenderType.CompositeRenderType =
		RenderType.create(
			"${AaronLib.MOD_ID}:quads_through_wall",
			DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.QUADS,
			1536,
			false,
			true,
			RenderType.CompositeState.builder()
				.setShaderState(POSITION_COLOR_SHADER)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setDepthTestState(NO_DEPTH_TEST)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false)
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALL_RENDER_TYPE
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALL_RENDER_TYPE

}
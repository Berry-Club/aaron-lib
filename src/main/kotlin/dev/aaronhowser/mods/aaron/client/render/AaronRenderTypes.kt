package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderStateShard.LineStateShard
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
				.setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
				.setLineState(LineStateShard(OptionalDouble.empty()))
				.setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
				.setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
				.setOutputState(RenderType.ITEM_ENTITY_TARGET)
				.setCullState(RenderType.NO_CULL)
				.setDepthTestState(RenderType.NO_DEPTH_TEST)
				.setWriteMaskState(RenderType.COLOR_WRITE)
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
				.setShaderState(RenderType.POSITION_COLOR_SHADER)
				.setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
				.setCullState(RenderType.NO_CULL)
				.setDepthTestState(RenderType.NO_DEPTH_TEST)
				.setWriteMaskState(RenderType.COLOR_WRITE)
				.createCompositeState(false)
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALL_RENDER_TYPE
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALL_RENDER_TYPE

}
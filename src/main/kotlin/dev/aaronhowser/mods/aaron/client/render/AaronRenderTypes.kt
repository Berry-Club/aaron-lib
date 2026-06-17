package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import org.lwjgl.opengl.GL11
import java.util.*

object AaronRenderTypes {

	private val ALWAYS_SUCCEED_DEPTH_TEST: DepthTestStateShard =
		object : DepthTestStateShard("always", GL11.GL_ALWAYS) {
			override fun setupRenderState() {
				RenderSystem.enableDepthTest()
				RenderSystem.depthFunc(GL11.GL_ALWAYS)
			}

			override fun clearRenderState() {
				RenderSystem.depthFunc(GL11.GL_LEQUAL)
			}
		}

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
				.setDepthTestState(ALWAYS_SUCCEED_DEPTH_TEST)
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
				.setDepthTestState(ALWAYS_SUCCEED_DEPTH_TEST)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false)
		)

	fun linesThroughWalls(): RenderType = LINES_THROUGH_WALL_RENDER_TYPE
	fun quadsThroughWalls(): RenderType = QUADS_THROUGH_WALL_RENDER_TYPE

}

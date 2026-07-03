package dev.aaronhowser.mods.aaron.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.sqrt

@Suppress("unused")
object AaronRenderUtil {

	private val HALF_SQRT_3: Float = (sqrt(3.0) / 2.0).toFloat()
	private const val FULL_BRIGHT: Int = 0xF000F0

	private fun defaultBufferSource(): MultiBufferSource.BufferSource {
		return Minecraft.getInstance()
			.renderBuffers()
			.bufferSource()
	}

	fun renderRaysDoubleLayer(
		poseStack: PoseStack,
		time: Float,
		bufferSource: MultiBufferSource,
		centerColor: Int = 0xFF000000.toInt(),
		outerColor: Int = 0x002C6A70,
		amountRays: Int = 15,
		rayLength: Float = 0.325f,
		rayWidth: Float = 0.15f
	) {
		renderDragonRays(
			poseStack = poseStack,
			time = time,
			bufferSource = bufferSource,
			centerColor = centerColor,
			outerColor = outerColor,
			amountRays = amountRays,
			rayLength = rayLength,
			rayWidth = rayWidth
		)

		renderDragonRaysDepth(
			poseStack = poseStack,
			time = time,
			bufferSource = bufferSource,
			centerColor = centerColor,
			outerColor = outerColor,
			amountRays = amountRays,
			rayLength = rayLength,
			rayWidth = rayWidth
		)
	}

	fun renderDragonRays(
		poseStack: PoseStack,
		time: Float,
		bufferSource: MultiBufferSource,
		centerColor: Int = 0xFF000000.toInt(),
		outerColor: Int = 0x002C6A70,
		amountRays: Int = 15,
		rayLength: Float = 0.325f,
		rayWidth: Float = 0.15f
	) {
		renderRays(
			poseStack = poseStack,
			time = time,
			vertexConsumer = bufferSource.getBuffer(RenderTypes.dragonRays()),
			centerColor = centerColor,
			outerColor = outerColor,
			amountRays = amountRays,
			rayLength = rayLength,
			rayWidth = rayWidth
		)
	}

	fun renderDragonRaysDepth(
		poseStack: PoseStack,
		time: Float,
		bufferSource: MultiBufferSource,
		centerColor: Int = 0xFF000000.toInt(),
		outerColor: Int = 0x002C6A70,
		amountRays: Int = 15,
		rayLength: Float = 0.325f,
		rayWidth: Float = 0.15f
	) {
		renderRays(
			poseStack = poseStack,
			time = time,
			vertexConsumer = bufferSource.getBuffer(RenderTypes.dragonRaysDepth()),
			centerColor = centerColor,
			outerColor = outerColor,
			amountRays = amountRays,
			rayLength = rayLength,
			rayWidth = rayWidth
		)
	}

	fun renderRays(
		poseStack: PoseStack,
		time: Float,
		vertexConsumer: VertexConsumer,
		centerColor: Int = 0xFF000000.toInt(),
		outerColor: Int = 0x002C6A70,
		amountRays: Int = 15,
		rayLength: Float = 0.325f,
		rayWidth: Float = 0.15f
	) {
		poseStack.pushPose()

		val randomSource = RandomSource.create(432L)
		val vec0 = Vector3f()
		val vec1 = Vector3f()
		val vec2 = Vector3f()
		val vec3 = Vector3f()
		val quaternionf = Quaternionf()

		for (rayIndex in 0 until amountRays) {
			quaternionf
				.identity()
				.rotateXYZ(
					randomSource.nextFloat() * (Math.PI * 2).toFloat(),
					randomSource.nextFloat() * (Math.PI * 2).toFloat(),
					randomSource.nextFloat() * (Math.PI * 2).toFloat()
				)
				.rotateXYZ(
					randomSource.nextFloat() * (Math.PI * 2).toFloat(),
					randomSource.nextFloat() * (Math.PI * 2).toFloat(),
					randomSource.nextFloat() * (Math.PI * 2).toFloat() + time * (Math.PI / 2).toFloat()
				)

			poseStack.mulPose(quaternionf)

			vec1.set(-HALF_SQRT_3 * rayWidth, rayLength, -0.5F * rayWidth)
			vec2.set(HALF_SQRT_3 * rayWidth, rayLength, -0.5F * rayWidth)
			vec3.set(0.0F, rayLength, rayWidth)

			val pose = poseStack.last()

			vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
			vertexConsumer.addVertex(pose, vec1).setColor(outerColor)
			vertexConsumer.addVertex(pose, vec2).setColor(outerColor)

			vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
			vertexConsumer.addVertex(pose, vec2).setColor(outerColor)
			vertexConsumer.addVertex(pose, vec3).setColor(outerColor)

			vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
			vertexConsumer.addVertex(pose, vec3).setColor(outerColor)
			vertexConsumer.addVertex(pose, vec1).setColor(outerColor)
		}

		poseStack.popPose()
	}

	fun renderLineThroughWalls(
		poseStack: PoseStack,
		start: Vec3,
		end: Vec3,
		color: Int
	) {
		renderLineThroughWalls(
			poseStack = poseStack,
			bufferSource = defaultBufferSource(),
			start = start,
			end = end,
			color = color
		)
	}

	fun renderLineThroughWalls(
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		start: Vec3,
		end: Vec3,
		color: Int
	) {
		val vertexConsumer = bufferSource
			.getBuffer(AaronRenderTypes.LINES_THROUGH_WALLS)

		val pose = poseStack.last()
		val normalVec = start.vectorTo(end).normalize()

		addVertex(
			pose,
			vertexConsumer,
			color,
			start.x.toFloat(), start.y.toFloat(), start.z.toFloat(),
			0f, 0f,
			normalX = normalVec.x.toFloat(),
			normalY = normalVec.y.toFloat(),
			normalZ = normalVec.z.toFloat()
		)

		addVertex(
			pose,
			vertexConsumer,
			color,
			end.x.toFloat(), end.y.toFloat(), end.z.toFloat(),
			0f, 0f,
			normalX = normalVec.x.toFloat(),
			normalY = normalVec.y.toFloat(),
			normalZ = normalVec.z.toFloat()
		)
	}

	fun renderCubeWireframe(
		poseStack: PoseStack,
		minX: Double,
		minY: Double,
		minZ: Double,
		maxX: Double,
		maxY: Double,
		maxZ: Double,
		color: Int
	) {
		renderCubeWireframe(
			poseStack = poseStack,
			minX = minX.toFloat(),
			minY = minY.toFloat(),
			minZ = minZ.toFloat(),
			maxX = maxX.toFloat(),
			maxY = maxY.toFloat(),
			maxZ = maxZ.toFloat(),
			color = color,
			renderType = RenderTypes.lines()
		)
	}

	fun renderCubeWireframeThroughWalls(
		poseStack: PoseStack,
		minX: Double,
		minY: Double,
		minZ: Double,
		maxX: Double,
		maxY: Double,
		maxZ: Double,
		color: Int
	) {
		renderCubeWireframe(
			poseStack = poseStack,
			minX = minX.toFloat(),
			minY = minY.toFloat(),
			minZ = minZ.toFloat(),
			maxX = maxX.toFloat(),
			maxY = maxY.toFloat(),
			maxZ = maxZ.toFloat(),
			color = color,
			renderType = AaronRenderTypes.LINES_THROUGH_WALLS
		)
	}

	private fun renderCubeWireframe(
		poseStack: PoseStack,
		minX: Float,
		minY: Float,
		minZ: Float,
		maxX: Float,
		maxY: Float,
		maxZ: Float,
		color: Int,
		renderType: RenderType
	) {
		val vertexConsumer = defaultBufferSource().getBuffer(renderType)
		val pose = poseStack.last()

		// X-axis edges
		addLine(pose, vertexConsumer, color, minX, minY, minZ, maxX, minY, minZ)
		addLine(pose, vertexConsumer, color, minX, minY, maxZ, maxX, minY, maxZ)
		addLine(pose, vertexConsumer, color, minX, maxY, minZ, maxX, maxY, minZ)
		addLine(pose, vertexConsumer, color, minX, maxY, maxZ, maxX, maxY, maxZ)

		// Z-axis edges
		addLine(pose, vertexConsumer, color, minX, minY, minZ, minX, minY, maxZ)
		addLine(pose, vertexConsumer, color, maxX, minY, minZ, maxX, minY, maxZ)
		addLine(pose, vertexConsumer, color, minX, maxY, minZ, minX, maxY, maxZ)
		addLine(pose, vertexConsumer, color, maxX, maxY, minZ, maxX, maxY, maxZ)

		// Y-axis edges
		addLine(pose, vertexConsumer, color, minX, minY, minZ, minX, maxY, minZ)
		addLine(pose, vertexConsumer, color, maxX, minY, minZ, maxX, maxY, minZ)
		addLine(pose, vertexConsumer, color, minX, minY, maxZ, minX, maxY, maxZ)
		addLine(pose, vertexConsumer, color, maxX, minY, maxZ, maxX, maxY, maxZ)
	}

	fun renderCubeThroughWalls(
		poseStack: PoseStack,
		minX: Double,
		minY: Double,
		minZ: Double,
		maxX: Double,
		maxY: Double,
		maxZ: Double,
		color: Int
	) {
		val vertexConsumer = defaultBufferSource()
			.getBuffer(AaronRenderTypes.QUADS_THROUGH_WALLS)
		val pose = poseStack.last()

		val minXFloat = minX.toFloat()
		val minYFloat = minY.toFloat()
		val minZFloat = minZ.toFloat()
		val maxXFloat = maxX.toFloat()
		val maxYFloat = maxY.toFloat()
		val maxZFloat = maxZ.toFloat()

		addQuad(
			pose, vertexConsumer, color,
			minXFloat, maxYFloat, maxZFloat,
			maxXFloat, maxYFloat, maxZFloat,
			maxXFloat, maxYFloat, minZFloat,
			minXFloat, maxYFloat, minZFloat
		)
		addQuad(
			pose, vertexConsumer, color,
			minXFloat, minYFloat, minZFloat,
			maxXFloat, minYFloat, minZFloat,
			maxXFloat, minYFloat, maxZFloat,
			minXFloat, minYFloat, maxZFloat
		)
		addQuad(
			pose, vertexConsumer, color,
			maxXFloat, minYFloat, minZFloat,
			minXFloat, minYFloat, minZFloat,
			minXFloat, maxYFloat, minZFloat,
			maxXFloat, maxYFloat, minZFloat
		)
		addQuad(
			pose, vertexConsumer, color,
			minXFloat, minYFloat, maxZFloat,
			maxXFloat, minYFloat, maxZFloat,
			maxXFloat, maxYFloat, maxZFloat,
			minXFloat, maxYFloat, maxZFloat
		)
		addQuad(
			pose, vertexConsumer, color,
			maxXFloat, minYFloat, maxZFloat,
			maxXFloat, minYFloat, minZFloat,
			maxXFloat, maxYFloat, minZFloat,
			maxXFloat, maxYFloat, maxZFloat
		)
		addQuad(
			pose, vertexConsumer, color,
			minXFloat, minYFloat, minZFloat,
			minXFloat, minYFloat, maxZFloat,
			minXFloat, maxYFloat, maxZFloat,
			minXFloat, maxYFloat, minZFloat
		)
	}

	fun renderTexturedCube(
		poseStack: PoseStack,
		renderType: RenderType,
		topTextureLocation: Identifier,
		bottomTextureLocation: Identifier,
		northTextureLocation: Identifier,
		southTextureLocation: Identifier,
		eastTextureLocation: Identifier,
		westTextureLocation: Identifier,
		light: Int = FULL_BRIGHT,
		overlay: Int = OverlayTexture.NO_OVERLAY
	) {
		renderTexturedCube(
			poseStack = poseStack,
			bufferSource = defaultBufferSource(),
			renderType = renderType,
			topTextureLocation = topTextureLocation,
			bottomTextureLocation = bottomTextureLocation,
			northTextureLocation = northTextureLocation,
			southTextureLocation = southTextureLocation,
			eastTextureLocation = eastTextureLocation,
			westTextureLocation = westTextureLocation,
			light = light,
			overlay = overlay
		)
	}

	fun renderTexturedCube(
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		renderType: RenderType,
		topTextureLocation: Identifier,
		bottomTextureLocation: Identifier,
		northTextureLocation: Identifier,
		southTextureLocation: Identifier,
		eastTextureLocation: Identifier,
		westTextureLocation: Identifier,
		light: Int = FULL_BRIGHT,
		overlay: Int = OverlayTexture.NO_OVERLAY
	) {
		val textureAtlas = Minecraft.getInstance().atlasManager.getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS)

		val topSprite = textureAtlas.getSprite(topTextureLocation)
		val bottomSprite = textureAtlas.getSprite(bottomTextureLocation)
		val westSprite = textureAtlas.getSprite(westTextureLocation)
		val eastSprite = textureAtlas.getSprite(eastTextureLocation)
		val northSprite = textureAtlas.getSprite(northTextureLocation)
		val southSprite = textureAtlas.getSprite(southTextureLocation)

		val map = mapOf(
			Direction.UP to topSprite,
			Direction.DOWN to bottomSprite,
			Direction.WEST to westSprite,
			Direction.EAST to eastSprite,
			Direction.NORTH to northSprite,
			Direction.SOUTH to southSprite,
		)

		val vertexConsumer = bufferSource.getBuffer(renderType)

		val pose = poseStack.last()

		for ((direction, sprite) in map) {
			val vertices = getVertices(direction, 1f, 1f, 1f)
			val normal = direction.unitVec3i

			for ((index, vector) in vertices.withIndex()) {
				val u = if (index == 0 || index == 3) sprite.u0 else sprite.u1
				val v = if (index == 0 || index == 1) sprite.v1 else sprite.v0

				addVertex(
					pose,
					vertexConsumer,
					0xFFFFFFFF.toInt(),
					vector.x, vector.y, vector.z,
					u, v,
					normalX = normal.x.toFloat(),
					normalY = normal.y.toFloat(),
					normalZ = normal.z.toFloat(),
					light = light,
					overlay = overlay
				)
			}
		}

	}

	@Deprecated("Use getVertices(direction, width, height, depth) instead")
	fun getVertices(direction: Direction, width: Float, length: Float): List<Vector3f> {
		return getVertices(direction, width, length, width)
	}

	fun getVertices(direction: Direction, width: Float, height: Float, depth: Float): List<Vector3f> {
		val bottomNorthWest = Vector3f(0f, 0f, 0f)
		val bottomNorthEast = Vector3f(width, 0f, 0f)
		val bottomSouthWest = Vector3f(0f, 0f, depth)
		val bottomSouthEast = Vector3f(width, 0f, depth)

		val topNorthWest = Vector3f(0f, height, 0f)
		val topNorthEast = Vector3f(width, height, 0f)
		val topSouthWest = Vector3f(0f, height, depth)
		val topSouthEast = Vector3f(width, height, depth)

		return when (direction) {
			Direction.UP -> listOf(topSouthWest, topSouthEast, topNorthEast, topNorthWest)
			Direction.DOWN -> listOf(bottomNorthWest, bottomNorthEast, bottomSouthEast, bottomSouthWest)
			Direction.NORTH -> listOf(bottomNorthEast, bottomNorthWest, topNorthWest, topNorthEast)
			Direction.SOUTH -> listOf(bottomSouthWest, bottomSouthEast, topSouthEast, topSouthWest)
			Direction.EAST -> listOf(bottomSouthEast, bottomNorthEast, topNorthEast, topSouthEast)
			Direction.WEST -> listOf(bottomNorthWest, bottomSouthWest, topSouthWest, topNorthWest)
		}
	}

	fun addColoredVertex(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x: Float,
		y: Float,
		z: Float,
	) {
		consumer.addVertex(pose.pose(), x, y, z)
			.setColor(color)
	}

	private fun addQuad(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x1: Float,
		y1: Float,
		z1: Float,
		x2: Float,
		y2: Float,
		z2: Float,
		x3: Float,
		y3: Float,
		z3: Float,
		x4: Float,
		y4: Float,
		z4: Float
	) {
		addColoredVertex(pose, consumer, color, x1, y1, z1)
		addColoredVertex(pose, consumer, color, x2, y2, z2)
		addColoredVertex(pose, consumer, color, x3, y3, z3)
		addColoredVertex(pose, consumer, color, x4, y4, z4)
	}

	private fun addLine(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x1: Float,
		y1: Float,
		z1: Float,
		x2: Float,
		y2: Float,
		z2: Float
	) {
		val normalX = x2 - x1
		val normalY = y2 - y1
		val normalZ = z2 - z1
		val length = sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ)
		if (length == 0f) return

		addLineVertex(pose, consumer, color, x1, y1, z1, normalX / length, normalY / length, normalZ / length)
		addLineVertex(pose, consumer, color, x2, y2, z2, normalX / length, normalY / length, normalZ / length)
	}

	private fun addLineVertex(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x: Float,
		y: Float,
		z: Float,
		normalX: Float,
		normalY: Float,
		normalZ: Float
	) {
		consumer.addVertex(pose.pose(), x, y, z)
			.setColor(color)
			.setNormal(pose, normalX, normalY, normalZ)
	}

	fun addVertex(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x: Float, y: Float, z: Float,
		u: Float, v: Float,
		normalX: Float = 0f,
		normalY: Float = 1f,
		normalZ: Float = 0f,
		light: Int = 15728880,
		overlay: Int = OverlayTexture.NO_OVERLAY,
	) {
		consumer.addVertex(pose.pose(), x, y, z)
			.setColor(color)
			.setUv(u, v)
			.setOverlay(overlay)
			.setLight(light)
			.setNormal(pose, normalX, normalY, normalZ)
	}

	fun addVertex(
		pose: PoseStack.Pose,
		consumer: VertexConsumer,
		color: Int,
		x: Float, y: Float, z: Float,
		u: Float, v: Float,
		u1: Int, v1: Int,
		u2: Int, v2: Int,
		normalX: Float = 0f,
		normalY: Float = 1f,
		normalZ: Float = 0f,
	) {
		consumer.addVertex(pose.pose(), x, y, z)
			.setColor(color)
			.setUv(u, v)
			.setUv1(u1, v1)
			.setUv2(u2, v2)
			.setNormal(pose, normalX, normalY, normalZ)
	}

	fun getColorFromFluid(fluidStack: FluidStack): Int {
		return 0xFFFFFFFF.toInt()
	}

	private val SPRITE_AVERAGE_COLOR_CACHE: MutableMap<TextureAtlasSprite, Int> = mutableMapOf()

	fun getSpriteAverageColor(sprite: TextureAtlasSprite): Int {
		val cachedColor = SPRITE_AVERAGE_COLOR_CACHE[sprite]
		if (cachedColor != null) return cachedColor

		val nativeImage = sprite.contents().originalImage
		val width = nativeImage.width
		val height = nativeImage.height

		var totalRed = 0
		var totalGreen = 0
		var totalBlue = 0
		var totalPixels = 0

		// There's some bullfuckery going on here, and I blame Mojang
		for (x in 0 until width) for (y in 0 until height) {
			val color = nativeImage.getPixel(x, y)

			val a = color ushr 24 and 0xFF
			if (a <= 0) continue

			val r = color ushr 16 and 0xFF
			val g = color ushr 8 and 0xFF
			val b = color and 0xFF

			totalRed += r
			totalGreen += g
			totalBlue += b
			totalPixels++
		}

		if (totalPixels == 0) return 0xFFFFFFFF.toInt()

		val averageRed = totalRed / totalPixels
		val averageGreen = totalGreen / totalPixels
		val averageBlue = totalBlue / totalPixels

		val averageColor = (0xFF shl 24) or (averageRed shl 16) or (averageGreen shl 8) or averageBlue
		SPRITE_AVERAGE_COLOR_CACHE[sprite] = averageColor
		return averageColor
	}

}

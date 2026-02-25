package dev.aaronhowser.mods.aaron.block_walker

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

// Copied largely from VidLib
data class PositionedBlock(
	val pos: BlockPos,
	val state: BlockState
) {

	companion object {
		val CODEC: MapCodec<PositionedBlock> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					BlockPos.CODEC
						.fieldOf("pos")
						.forGetter(PositionedBlock::pos),
					BlockState.CODEC
						.fieldOf("state")
						.forGetter(PositionedBlock::state)
				).apply(instance, ::PositionedBlock)
			}
	}

}
package dev.aaronhowser.mods.aaron.block

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.serialization.AaronExtraCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.StreamCodec
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

		val STREAM_CODEC: StreamCodec<ByteBuf, PositionedBlock> =
			StreamCodec.composite(
				BlockPos.STREAM_CODEC, PositionedBlock::pos,
				AaronExtraCodecs.BLOCK_STATE_STREAM_CODEC, PositionedBlock::state,
				::PositionedBlock
			)
	}

}
package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.stream.Stream

class TagBlockStateIngredient(
	val tag: TagKey<Block>
) : BlockStateIngredient() {

	override fun test(state: BlockState): Boolean = state.isBlock(tag)

	override fun generateStates(): Stream<BlockState> {
		return BuiltInRegistries.BLOCK
			.getTag(tag)
			.stream()
			.flatMap(HolderSet<Block>::stream)
			.map { it.value().defaultBlockState() }
	}

	override val isSimple: Boolean = true

	companion object {
		val CODEC: MapCodec<TagBlockStateIngredient> =
			TagKey.codec(Registries.BLOCK)
				.xmap(::TagBlockStateIngredient, TagBlockStateIngredient::tag)
				.fieldOf("tag")
	}

}
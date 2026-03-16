package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.stream.Stream

class SingleBlockIngredient(
	val block: Block
) : BlockStateIngredient() {

	override val isSimple: Boolean = true

	override fun test(state: BlockState): Boolean = state.isBlock(block)

	override fun generateStates(): Stream<BlockState> = Stream.of(block.defaultBlockState())

	override fun getType(): BlockStateIngredientType<*> {
		TODO("Not yet implemented")
	}

	override fun hashCode(): Int {
		return block.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is SingleBlockIngredient) return false

		if (block != other.block) return false
		return true
	}
}
package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate
import java.util.stream.Stream

abstract class BlockStateIngredient : Predicate<BlockState> {

	abstract override fun test(state: BlockState): Boolean

	protected abstract fun generateStates(): Stream<BlockState>
	protected abstract fun getType(): BlockStateIngredientType<*>
	protected abstract val isSimple: Boolean

	val blockStates: List<BlockState> by lazy {
		generateStates().toList()
	}

}
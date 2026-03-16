package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
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

	companion object {
		fun empty() = EmptyBlockStateIngredient
		fun of() = empty()
		fun of(tag: TagKey<Block>) = TagBlockStateIngredient(tag)
	}

}
package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import dev.aaronhowser.mods.aaron.registry.actual.AaronBlockStateIngredientTypeRegistry
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.function.Predicate
import java.util.stream.Stream

abstract class BlockStateIngredient : Predicate<BlockState> {

	val blockStates: List<BlockState> by lazy {
		generateStates().toList()
	}

	abstract override fun test(state: BlockState): Boolean
	abstract override fun hashCode(): Int
	abstract override fun equals(other: Any?): Boolean

	protected abstract fun generateStates(): Stream<BlockState>
	protected abstract fun getType(): BlockStateIngredientType<*>
	protected abstract val isSimple: Boolean

	fun hasNoStates(): Boolean = blockStates.isEmpty()

	companion object {
		val MAP_CODEC_NONEMPTY =
			NeoForgeExtraCodecs.dispatchMapOrElse(
				AaronBlockStateIngredientTypeRegistry.BLOCK_STATE_INGREDIENT_TYPES.byNameCodec(),

			)

		fun empty() = EmptyBlockStateIngredient
		fun of() = empty()
		fun of(tag: TagKey<Block>) = TagBlockStateIngredient(tag)
	}

}
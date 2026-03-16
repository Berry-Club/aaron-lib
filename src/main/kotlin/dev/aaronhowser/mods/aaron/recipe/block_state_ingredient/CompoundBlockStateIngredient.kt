package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.registry.actual.AaronBlockStateIngredientTypeRegistry
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.stream.Stream

class CompoundBlockStateIngredient(
	val children: List<BlockStateIngredient>
) : BlockStateIngredient() {

	init {
		require(children.isNotEmpty()) { "CompoundBlockStateIngredient cannot be empty" }
	}

	override val isSimple: Boolean = children.all { it.isSimple }

	override fun test(state: BlockState): Boolean {
		return children.any { it.test(state) }
	}

	override fun generateStates(): Stream<BlockState> {
		return children.stream().flatMap(BlockStateIngredient::generateStates)
	}

	override fun getType(): BlockStateIngredientType<*> = AaronBlockStateIngredientTypeRegistry.COMPOUND.get()

	override fun hashCode(): Int = children.hashCode()
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is CompoundBlockStateIngredient) return false

		return children == other.children
	}

	companion object {
		val CODEC: MapCodec<CompoundBlockStateIngredient> =
			NeoForgeExtraCodecs
				.aliasedFieldOf(LIST_CODEC_NON_EMPTY, "children", "ingredients")
				.xmap(::CompoundBlockStateIngredient, CompoundBlockStateIngredient::children)
	}

}
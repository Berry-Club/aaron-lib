package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.registry.actual.AaronBlockStateIngredientTypeRegistry
import net.minecraft.world.level.block.state.BlockState
import java.util.stream.Stream

object EmptyBlockStateIngredient : BlockStateIngredient() {

	val CODEC: MapCodec<EmptyBlockStateIngredient> = MapCodec.unit(this)

	override fun test(state: BlockState): Boolean = state.isAir
	override fun generateStates(): Stream<BlockState> = Stream.empty()
	override fun getType(): BlockStateIngredientType<*> = AaronBlockStateIngredientTypeRegistry.EMPTY.get()
	override val isSimple: Boolean = true

	override fun hashCode(): Int = 0
	override fun equals(other: Any?): Boolean {
		return this === other
	}

}
package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.registry.actual.AaronBlockStateIngredientTypeRegistry
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

	override val isSimple: Boolean = true

	override fun test(state: BlockState): Boolean = state.isBlock(tag)

	override fun generateStates(): Stream<BlockState> {
		return BuiltInRegistries.BLOCK
			.getTag(tag)
			.stream()
			.flatMap(HolderSet<Block>::stream)
			.map { it.value().defaultBlockState() }
	}

	override fun getType(): BlockStateIngredientType<*> = AaronBlockStateIngredientTypeRegistry.TAG.get()

	override fun hashCode(): Int {
		return tag.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is TagBlockStateIngredient) return false

		if (tag != other.tag) return false

		return true
	}

	companion object {
		val CODEC: MapCodec<TagBlockStateIngredient> =
			TagKey.codec(Registries.BLOCK)
				.xmap(::TagBlockStateIngredient, TagBlockStateIngredient::tag)
				.fieldOf("tag")
	}

}
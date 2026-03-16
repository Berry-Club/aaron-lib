package dev.aaronhowser.mods.aaron.registry.actual

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.recipe.block_state_ingredient.BlockStateIngredientType
import dev.aaronhowser.mods.aaron.recipe.block_state_ingredient.EmptyBlockStateIngredient
import dev.aaronhowser.mods.aaron.recipe.block_state_ingredient.TagBlockStateIngredient
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import java.util.function.Supplier

object AaronBlockStateIngredientTypeRegistry {

	val KEY: ResourceKey<Registry<BlockStateIngredientType<*>>> =
		ResourceKey.createRegistryKey(AaronLib.modResource("block_state_ingredient_type"))

	val BUILDER: Registry<BlockStateIngredientType<*>> =
		RegistryBuilder(KEY).sync(true).create()

	val BLOCK_STATE_INGREDIENT_TYPES: DeferredRegister<BlockStateIngredientType<*>> =
		DeferredRegister.create(KEY, AaronLib.MOD_ID)

	val TAG: DeferredHolder<BlockStateIngredientType<*>, BlockStateIngredientType<TagBlockStateIngredient>> =
		BLOCK_STATE_INGREDIENT_TYPES.register("tag", Supplier { BlockStateIngredientType(TagBlockStateIngredient.CODEC) })
	val EMPTY: DeferredHolder<BlockStateIngredientType<*>, BlockStateIngredientType<*>> =
		BLOCK_STATE_INGREDIENT_TYPES.register("empty", Supplier { BlockStateIngredientType(EmptyBlockStateIngredient.CODEC) })

}
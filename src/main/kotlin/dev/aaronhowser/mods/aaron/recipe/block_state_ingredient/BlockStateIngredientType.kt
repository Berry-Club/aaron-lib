package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class BlockStateIngredientType<T : BlockStateIngredient>(
	val codec: MapCodec<T>,
	val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
) {
	constructor(codec: MapCodec<T>) : this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()))
}
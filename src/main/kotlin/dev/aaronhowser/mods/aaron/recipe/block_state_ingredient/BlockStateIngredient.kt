package dev.aaronhowser.mods.aaron.recipe.block_state_ingredient

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.registry.actual.AaronBlockStateIngredientTypeRegistry
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

	abstract fun generateStates(): Stream<BlockState>
	abstract fun getType(): BlockStateIngredientType<*>
	abstract val isSimple: Boolean

	fun hasNoStates(): Boolean = blockStates.isEmpty()
	fun isEmpty(): Boolean = this === empty()

	companion object {
		val SINGLE_OR_TAG_CODEC: MapCodec<BlockStateIngredient> = singleOrTagCodec()
		val MAP_CODEC_NONEMPTY: MapCodec<BlockStateIngredient> = makeMapCodec()

		val MAP_CODEC_CODEC: Codec<BlockStateIngredient> = MAP_CODEC_NONEMPTY.codec()

		val LIST_CODEC: Codec<List<BlockStateIngredient>> = MAP_CODEC_CODEC.listOf()
		val LIST_CODEC_NON_EMPTY: Codec<List<BlockStateIngredient>> =
			LIST_CODEC.validate { list ->
				if (list.isEmpty()) {
					return@validate DataResult.error { "BlockStateIngredient list cannot be empty" }
				}

				return@validate DataResult.success(list)
			}

		val CODEC: Codec<BlockStateIngredient> = codec(allowEmpty = true)
		val CODEC_NON_EMPTY: Codec<BlockStateIngredient> = codec(allowEmpty = false)

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlockStateIngredient> =
			object : StreamCodec<RegistryFriendlyByteBuf, BlockStateIngredient> {
				val DISPATCH_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlockStateIngredient> =
					ByteBufCodecs
						.registry(AaronBlockStateIngredientTypeRegistry.KEY)
						.dispatch(BlockStateIngredient::getType, BlockStateIngredientType<*>::streamCodec)

				val BLOCK_LIST_CODEC: StreamCodec<RegistryFriendlyByteBuf, List<Block>> =
					ByteBufCodecs
						.registry(Registries.BLOCK)
						.apply(ByteBufCodecs.collection(NonNullList<Block>::createWithCapacity))

				override fun encode(buf: RegistryFriendlyByteBuf, ingredient: BlockStateIngredient) {
					if (ingredient.isSimple) {
						BLOCK_LIST_CODEC.encode(buf, ingredient.blockStates.toList())
					}
				}

				override fun decode(buf: RegistryFriendlyByteBuf): BlockStateIngredient {
					val size = buf.readVarInt()

					if (size == -1) {
						return DISPATCH_CODEC.decode(buf)
					}

				}

			}

		fun empty() = EmptyBlockStateIngredient
		fun of() = empty()
		fun of(tag: TagKey<Block>) = TagBlockStateIngredient(tag)

		private fun singleOrTagCodec(): MapCodec<BlockStateIngredient> {
			return MapCodec.recursive("BlockStateIngredient.SINGLE_OR_TAG_CODEC") {
				NeoForgeExtraCodecs.xor(
					SingleBlockIngredient.CODEC,
					TagBlockStateIngredient.CODEC,
				).xmap(
					{ either ->
						either.map({ it }, { it })
					},
					{ ingredient ->
						when (ingredient) {
							is SingleBlockIngredient -> Either.left(ingredient)
							is TagBlockStateIngredient -> Either.right(ingredient)
							else -> throw IllegalStateException()
						}
					}
				)
			}
		}

		private fun makeMapCodec(): MapCodec<BlockStateIngredient> {
			return NeoForgeExtraCodecs.dispatchMapOrElse(
				AaronBlockStateIngredientTypeRegistry.BUILDER.byNameCodec(),
				BlockStateIngredient::getType,
				BlockStateIngredientType<*>::codec,
				SINGLE_OR_TAG_CODEC
			).xmap(
				{ either -> either.map({ it }, { it }) },
				{ ingredient ->
					if (ingredient is SingleBlockIngredient || ingredient is TagBlockStateIngredient) {
						Either.right(ingredient)
					} else {
						Either.left(ingredient)
					}
				}
			).validate { ingredient ->
				if (ingredient.isEmpty()) {
					return@validate DataResult.error { "Cannot serialize an empty BlockStateIngredient" }
				}

				return@validate DataResult.success(ingredient)
			}
		}

		private fun codec(allowEmpty: Boolean): Codec<BlockStateIngredient> {
			val listCodec = Codec.lazyInitialized { if (allowEmpty) LIST_CODEC else LIST_CODEC_NON_EMPTY }

			return Codec.either(listCodec, MAP_CODEC_CODEC)
				.xmap(
					{ either ->
						either.map(::CompoundBlockStateIngredient, { it })
					},
					{ ingredient ->
						if (ingredient is CompoundBlockStateIngredient) {
							return@xmap Either.left(ingredient.children)
						} else if (ingredient.isEmpty()) {
							return@xmap Either.left(emptyList())
						}

						return@xmap Either.right(ingredient)
					}
				)
		}
	}

}
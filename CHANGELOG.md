# 1.7.0

- Fixed ScheduledTaskHandler not handling tasks scheduled with 0 or lower delay
- FluidState#isFluid
- AaronRecipeProvider#shapelessRecipe
- CompoundTag#putUuidIfNotNull
- ObservableMutableSet
- FluidStack#isFluid
- AaronUtil#dropStackAt
- Vector3f#toVec3
- AaronDataComponentRegistry#uuid
- Long#toBlockPos
- Long#toChunkPos
- ItemStack#asIngredient
- IntrinsicHolderTagsProvider$IntrinsicTagAppender<T>#add(vararg values: Holder<T>)
- Block walker stuff
- ItemStack#setUnit
- AaronDataComponentRegistry#registryKey
- AaronDataComponentRegistry#registryHolder
- ModConfigSpec$Builder#section
- ModelBuilder<T>#element
- ModelBuilder<T>$ElementBuilder#face
- ExtractOnlyInvWrapper
- ItemModelBuilder#override

# 1.6.0

- ItemStack#hasEnchantment
- ImprovedEntityPredicate
- Moved many classes

# 1.5.0

- weakMutableSet()
- Entity#isMovingHorizontally()
- Player#giveOrDropStack
- Either#isLeft
- Either#isRight
- AaronUtil#cleanEntityNbt
- Entity#getMinimalTag
- ItemLike#getDefaultInstance
- List<ItemStack>#totalCount
- ItemStack#isNotFull
- AaronUtil#flattenStacks
- Entity#getPovResult
- Holder<Potion>#getAsStack()
- AaronExtraCodecs.UINT_CODEC
- AaronExtraCodecs.UINT_STREAM_CODEC
- AaronDataComponentRegistry#uint
- AaronMobEffectsRegistry
- AaronAdvancementSubProvider

# 1.4.0

### Added

- `/aaron heal`
- Number.toDegrees()
- Number.toRadians()
- Style.withHoverText
- Style.withHoverText
- Style.withClickToRunCommand
- Style.withClickToSuggestCommand
- Style.withClickToOpenUrl
- Style.withClickToCopyToClipboard
- ItemStack.isItem
- BlockBehavior.BlockStateBase.isBlock
- Holder<T>.isHolder
- DamageSource.isDamageSource
- EntityType<*>.isEntity
- Entity.isEntity

# 1.3.0

### Added

- RandomSource.chance

# 1.2.0

### Added

- AaronUtil.getCachedUuid(playerUsernameString)
- Helpers for registration classes
- More packet stuff
- Data Component functions

# 1.1.0

### Added

- Entity.isServerSide

### Changed

- Schedulers are now per Level, rather than just a singular static global one
	- Access from SchedulerExtensions class
	- This was fairly directly ripped out of Lat's VidLib

# 1.0.1

### Fixed

- A couple classes I took from Irregular Implements were still marked as being from II, rather than from here

# 1.0.0

Initial release!
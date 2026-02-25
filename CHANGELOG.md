# 1.7.0

- ItemStack#hasEnchantment
- FluidState#isFluid
- CompoundTag#putUuidIfNotNull
- Vector3f#toVec3
- Long#toBlockPos
- Long#toChunkPos
- Update the recipe provider
- Moved some classes around
- IntrinsicHolderTagsProvider$IntrinsicTagAppender#add(vararg values: Holder)
- Block Walker stuff

# 1.5.3

### Fixed

- Obfuscate the jar so it works outside of dev

# 1.5.2

### Fixed

- ACTUALLY fix it this time

# 1.5.1

### Fixed

- Fixed the mixins not loading outside of dev

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
- AaronDataComponentRegistry#uint
- AaronMobEffectsRegistry
- AaronAdvancementSubProvider
- Holder<*>#getLocationOrNull
- RegistryObject<out Item>#getDefaultInstance()
- AaronExtraCodecs.COMPONENT_CODEC
- ItemLike#withComponent
- ItemStack#withComponent
- ItemStack#removeComponent
- AttributeInstance#hasModifier
- AaronExtraCodecs.ATTRIBUTE_MODIFIER_CODEC
- Entity#registryAccess
- ItemStack#partialNbtIngredient
- ItemStack#strictNbtIngredient

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
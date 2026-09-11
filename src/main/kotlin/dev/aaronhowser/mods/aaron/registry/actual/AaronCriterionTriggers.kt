package dev.aaronhowser.mods.aaron.registry.actual

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.advancement.BlockBrokenTrigger
import dev.aaronhowser.mods.aaron.advancement.PlayerActionTrigger
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object AaronCriterionTriggers {

	val TRIGGER_REGISTRY: DeferredRegister<CriterionTrigger<*>> =
		DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, AaronLib.MOD_ID)

	val BLOCK_BROKEN: DeferredHolder<CriterionTrigger<*>, BlockBrokenTrigger> =
		TRIGGER_REGISTRY.register("block_broken", ::BlockBrokenTrigger)

	val PLAYER_ACTION: DeferredHolder<CriterionTrigger<*>, PlayerActionTrigger> =
		TRIGGER_REGISTRY.register("player_action", ::PlayerActionTrigger)

}
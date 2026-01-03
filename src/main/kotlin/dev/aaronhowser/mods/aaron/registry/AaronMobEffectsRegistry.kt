package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.effect.MobEffect
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronMobEffectsRegistry {

	abstract fun getMobEffectRegistry(): DeferredRegister<MobEffect>

	fun register(id: String, supplier: Supplier<MobEffect>): DeferredHolder<MobEffect, MobEffect> {
		return getMobEffectRegistry().register(id, supplier)
	}

	fun register(id: String, mobEffect: MobEffect): DeferredHolder<MobEffect, MobEffect> {
		return register(id) { mobEffect }
	}

}
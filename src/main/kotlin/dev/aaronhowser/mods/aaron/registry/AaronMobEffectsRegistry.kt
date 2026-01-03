package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.effect.MobEffect
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronMobEffectsRegistry {

	abstract fun getMobEffectRegistry(): DeferredRegister<MobEffect>

	fun <T : MobEffect> register(id: String, supplier: Supplier<T>): DeferredHolder<MobEffect, T> {
		return getMobEffectRegistry().register(id, supplier)
	}

	fun <T : MobEffect> register(id: String, mobEffect: T): DeferredHolder<MobEffect, T> {
		return register(id) { mobEffect }
	}

}
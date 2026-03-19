package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronMobEffectsRegistry {

	abstract fun getMobEffectRegistry(): DeferredRegister<MobEffect>

	fun <T : MobEffect> register(id: String, supplier: Supplier<T>): DeferredHolder<MobEffect, T> {
		return getMobEffectRegistry().register(id, supplier)
	}

	fun registerSimple(
		id: String,
		category: MobEffectCategory,
		color: Int
	): DeferredHolder<MobEffect, out MobEffect> {
		return register(id) { object : MobEffect(category, color) {} }
	}

	fun registerSimpleInstantaneous(
		id: String,
		category: MobEffectCategory,
		color: Int
	): DeferredHolder<MobEffect, MobEffect> {
		return register(id) {
			object : MobEffect(category, color) {
				override fun isInstantenous(): Boolean = true
			}
		}
	}

}
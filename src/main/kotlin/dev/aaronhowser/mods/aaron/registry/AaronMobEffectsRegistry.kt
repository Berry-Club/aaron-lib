package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

abstract class AaronMobEffectsRegistry {

	abstract fun getMobEffectRegistry(): DeferredRegister<MobEffect>

	fun <T : MobEffect> register(id: String, supplier: Supplier<T>): RegistryObject<T> {
		return getMobEffectRegistry().register(id, supplier)
	}

}
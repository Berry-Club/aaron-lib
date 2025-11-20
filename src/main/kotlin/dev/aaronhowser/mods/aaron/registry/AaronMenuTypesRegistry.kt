package dev.aaronhowser.mods.aaron.registry

import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronMenuTypesRegistry {

	abstract fun getMenuTypeRegistry(): DeferredRegister<MenuType<*>>

	protected fun <T : AbstractContainerMenu> register(
		name: String,
		constructor: MenuType.MenuSupplier<T>
	): DeferredHolder<MenuType<*>, MenuType<T>> {
		return getMenuTypeRegistry().register(name, Supplier { MenuType(constructor, FeatureFlags.DEFAULT_FLAGS) })
	}

	abstract fun registerScreens(event: RegisterMenuScreensEvent)

}
package dev.aaronhowser.mods.aaron.registry

import dev.aaronhowser.mods.aaron.AaronExtensions.asIngredient
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

abstract class AaronArmorMaterialRegistry {

	abstract fun getArmorMaterialRegistry(): DeferredRegister<ArmorMaterial>

	private inner class Builder(
		id: String
	) {
		private val armorMap: MutableMap<ArmorItem.Type, Int> = mutableMapOf()

		private var toughness = 0f
		private var knockbackResist = 0f
		private var enchantValue = 10
		private var equipSound: Holder<SoundEvent> = SoundEvents.ARMOR_EQUIP_GENERIC
		private var repairIngredient = Supplier { Ingredient.EMPTY }
		private val layers = mutableListOf<ArmorMaterial.Layer>()

		init {
			val registryName = getArmorMaterialRegistry().namespace
			layers.add(ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(registryName, id)))
		}

		fun enchantValue(value: Int): Builder {
			enchantValue = value
			return this
		}

		fun boot(armorAmount: Int): Builder {
			armorMap[ArmorItem.Type.BOOTS] = armorAmount
			return this
		}

		fun leg(armorAmount: Int): Builder {
			armorMap[ArmorItem.Type.LEGGINGS] = armorAmount
			return this
		}

		fun chestplate(armorAmount: Int): Builder {
			armorMap[ArmorItem.Type.CHESTPLATE] = armorAmount
			return this
		}

		fun helmet(armorAmount: Int): Builder {
			armorMap[ArmorItem.Type.HELMET] = armorAmount
			return this
		}

		fun armor(armorAmount: Int): Builder {
			helmet(armorAmount)
			chestplate(armorAmount)
			leg(armorAmount)
			boot(armorAmount)

			return this
		}

		fun repair(tag: TagKey<Item>): Builder {
			repairIngredient = Supplier { tag.asIngredient() }
			return this
		}

		fun repair(itemHolder: Holder<Item>): Builder {
			repairIngredient = Supplier { itemHolder.value().asIngredient() }
			return this
		}

		fun addLayer(layer: ArmorMaterial.Layer): Builder {
			layers.add(layer)
			return this
		}

		fun build(): ArmorMaterial {
			return ArmorMaterial(
				armorMap,
				enchantValue,
				equipSound,
				repairIngredient,
				layers,
				toughness,
				knockbackResist
			)
		}
	}

}
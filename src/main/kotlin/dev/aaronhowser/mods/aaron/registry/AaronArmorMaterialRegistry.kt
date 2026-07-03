package dev.aaronhowser.mods.aaron.registry

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets

abstract class AaronArmorMaterialRegistry {

	abstract fun getArmorMaterialNamespace(): String

	protected inner class Builder(
		private val path: String
	) {
		private val armorMap: MutableMap<ArmorType, Int> = mutableMapOf()

		private var durability = 15
		private var toughness = 0f
		private var knockbackResist = 0f
		private var enchantValue = 10
		private var equipSound: Holder<SoundEvent> = SoundEvents.ARMOR_EQUIP_GENERIC
		private var repairIngredient: TagKey<Item>? = null
		private var assetId: ResourceKey<EquipmentAsset> =
			ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(getArmorMaterialNamespace(), path))

		fun durability(value: Int): Builder {
			durability = value
			return this
		}

		fun enchantValue(value: Int): Builder {
			enchantValue = value
			return this
		}

		fun boot(armorAmount: Int): Builder {
			armorMap[ArmorType.BOOTS] = armorAmount
			return this
		}

		fun leg(armorAmount: Int): Builder {
			armorMap[ArmorType.LEGGINGS] = armorAmount
			return this
		}

		fun chestplate(armorAmount: Int): Builder {
			armorMap[ArmorType.CHESTPLATE] = armorAmount
			return this
		}

		fun helmet(armorAmount: Int): Builder {
			armorMap[ArmorType.HELMET] = armorAmount
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
			repairIngredient = tag
			return this
		}

		fun asset(id: ResourceKey<EquipmentAsset>): Builder {
			assetId = id
			return this
		}

		fun asset(path: String): Builder {
			assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(getArmorMaterialNamespace(), path))
			return this
		}

		fun build(): ArmorMaterial {
			return ArmorMaterial(
				durability,
				armorMap,
				enchantValue,
				equipSound,
				toughness,
				knockbackResist,
				requireNotNull(repairIngredient) { "Armor material '$path' must define a repair ingredient tag." },
				assetId
			)
		}
	}

}

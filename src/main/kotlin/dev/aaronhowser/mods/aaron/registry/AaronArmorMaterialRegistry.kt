package dev.aaronhowser.mods.aaron.registry

import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

abstract class AaronArmorMaterialRegistry {

	abstract fun getArmorMaterialRegistry(): DeferredRegister<ArmorMaterial>

	protected inner class Builder(
		private val path: String
	) {
		private val armorMap: MutableMap<ArmorType, Int> = mutableMapOf()

		private var durability = 15
		private var toughness = 0f
		private var knockbackResist = 0f
		private var enchantValue = 10
		private var equipSound: Holder<SoundEvent> = SoundEvents.ARMOR_EQUIP_GENERIC
		private var repairTag: TagKey<Item> = Tags.Items.INGOTS_IRON
		private var equipmentAsset: ResourceKey<EquipmentAsset> = ResourceKey.create(
			EquipmentAssets.ROOT_ID,
			Identifier.fromNamespaceAndPath(getArmorMaterialRegistry().namespace, path)
		)

		fun durability(value: Int): Builder {
			durability = value
			return this
		}

		fun enchantValue(value: Int): Builder {
			enchantValue = value
			return this
		}

		fun toughness(value: Float): Builder {
			toughness = value
			return this
		}

		fun knockbackResist(value: Float): Builder {
			knockbackResist = value
			return this
		}

		fun equipSound(sound: Holder<SoundEvent>): Builder {
			equipSound = sound
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
			repairTag = tag
			return this
		}

		fun equipmentAsset(key: ResourceKey<EquipmentAsset>): Builder {
			equipmentAsset = key
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
				repairTag,
				equipmentAsset
			)
		}

		fun register(): DeferredHolder<ArmorMaterial, ArmorMaterial> {
			return getArmorMaterialRegistry().register(path, ::build)
		}
	}
}
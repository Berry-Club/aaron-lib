package dev.aaronhowser.mods.aaron.fake_player

import com.mojang.authlib.GameProfile
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.FakePlayer
import java.util.EnumMap

open class AttributeFakePlayer(
	level: ServerLevel,
	gameProfile: GameProfile
) : FakePlayer(level, gameProfile) {

	// Superclass construction can call overridden attribute methods before this property is initialized.
	@Suppress("RedundantNullableReturnType")
	private val previousEquipment: MutableMap<EquipmentSlot, ItemStack>? = EnumMap(EquipmentSlot::class.java)
	private var isUpdatingAttributes = false

	override fun getAttribute(attribute: Holder<Attribute>): AttributeInstance? {
		updateEquipmentAttributes()
		return super.getAttribute(attribute)
	}

	override fun getAttributeValue(attribute: Holder<Attribute>): Double {
		updateEquipmentAttributes()
		return super.getAttributeValue(attribute)
	}

	fun updateEquipmentAttributes() {
		if (isUpdatingAttributes) return
		if (previousEquipment == null) return

		isUpdatingAttributes = true
		try {
			for (equipmentSlot in EquipmentSlot.entries) {
				updateEquipmentSlotAttributes(equipmentSlot)
			}
		} finally {
			isUpdatingAttributes = false
		}
	}

	private fun updateEquipmentSlotAttributes(equipmentSlot: EquipmentSlot) {
		val previousEquipment = previousEquipment ?: return
		val previousStack = previousEquipment[equipmentSlot] ?: ItemStack.EMPTY
		val currentStack = getItemBySlot(equipmentSlot)
		if (ItemStack.matches(previousStack, currentStack)) return

		removeAttributeModifiers(previousStack, equipmentSlot)
		addAttributeModifiers(currentStack, equipmentSlot)
		previousEquipment[equipmentSlot] = currentStack.copy()
	}

	private fun removeAttributeModifiers(stack: ItemStack, equipmentSlot: EquipmentSlot) {
		if (stack.isEmpty) return

		fun removeModifier(attribute: Holder<Attribute>, modifier: AttributeModifier) {
			attributes
				.getInstance(attribute)
				?.removeModifier(modifier.id)
		}

		stack.forEachModifier(equipmentSlot, ::removeModifier)
	}

	private fun addAttributeModifiers(stack: ItemStack, equipmentSlot: EquipmentSlot) {
		if (stack.isEmpty) return

		fun addModifier(attribute: Holder<Attribute>, modifier: AttributeModifier) {
			val attributeInstance = attributes.getInstance(attribute) ?: return
			attributeInstance.removeModifier(modifier.id)
			attributeInstance.addTransientModifier(modifier)
		}

		stack.forEachModifier(equipmentSlot, ::addModifier)
	}
}
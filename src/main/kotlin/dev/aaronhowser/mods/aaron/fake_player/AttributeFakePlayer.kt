package dev.aaronhowser.mods.aaron.fake_player

import com.mojang.authlib.GameProfile
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.FakePlayer
import java.util.*

open class AttributeFakePlayer(
	level: ServerLevel,
	gameProfile: GameProfile
) : FakePlayer(level, gameProfile) {

	private val previousEquipment: MutableMap<EquipmentSlot, ItemStack> = EnumMap(EquipmentSlot::class.java)
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
		val previousStack = previousEquipment[equipmentSlot] ?: ItemStack.EMPTY
		val currentStack = getItemBySlot(equipmentSlot)
		if (ItemStack.matches(previousStack, currentStack)) return

		removeAttributeModifiers(previousStack, equipmentSlot)
		addAttributeModifiers(currentStack, equipmentSlot)
		previousEquipment[equipmentSlot] = currentStack.copy()
	}

	private fun removeAttributeModifiers(stack: ItemStack, equipmentSlot: EquipmentSlot) {
		if (stack.isEmpty) return

		stack.forEachModifier(equipmentSlot) { attribute, modifier ->
			attributes
				.getInstance(attribute)
				?.removeModifier(modifier.id)
		}
	}

	private fun addAttributeModifiers(stack: ItemStack, equipmentSlot: EquipmentSlot) {
		if (stack.isEmpty) return

		stack.forEachModifier(equipmentSlot) { attribute, modifier ->
			val attributeInstance = attributes.getInstance(attribute) ?: return@forEachModifier
			attributeInstance.removeModifier(modifier.id)
			attributeInstance.addTransientModifier(modifier)
		}
	}
}
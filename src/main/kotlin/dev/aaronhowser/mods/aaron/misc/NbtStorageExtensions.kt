package dev.aaronhowser.mods.aaron.misc

import dev.aaronhowser.mods.aaron.misc.AaronUtil.cleanEntityNbt
import net.minecraft.core.NonNullList
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ProblemReporter
import net.minecraft.world.ContainerHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import java.util.*

@Suppress("unused")
object NbtStorageExtensions {
	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		return this.getIntArray(key)
			.filter { it.size == 4 }
			.map(UUIDUtil::uuidFromIntArray)
			.orElse(null)
	}

	fun CompoundTag.putUuidIfNotNull(key: String, uuid: UUID?): CompoundTag {
		if (uuid != null) this.putIntArray(key, UUIDUtil.uuidToIntArray(uuid))
		return this
	}

	fun Entity.getMinimalTag(stripUniqueness: Boolean = true): CompoundTag {
		val output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.registryAccess())
		this.save(output)
		val nbt = output.buildResult()
		cleanEntityNbt(nbt, stripUniqueness)
		return nbt
	}

	fun ValueOutput.saveItems(items: NonNullList<ItemStack>) {
		ContainerHelper.saveAllItems(this, items)
	}

	fun ValueOutput.saveItems(container: SimpleContainer) {
		saveItems(container.items)
	}

	fun ValueInput.loadItems(items: NonNullList<ItemStack>) {
		ContainerHelper.loadAllItems(this, items)
	}

	fun ValueInput.loadItems(container: SimpleContainer) {
		loadItems(container.items)
	}

	fun ValueOutput.saveEnergy(name: String, energyStorage: SimpleEnergyHandler) {
		energyStorage.serialize(this.child(name))
	}

	fun ValueInput.loadEnergy(name: String, energyStorage: SimpleEnergyHandler) {
		this.child(name).ifPresent(energyStorage::deserialize)
	}
}

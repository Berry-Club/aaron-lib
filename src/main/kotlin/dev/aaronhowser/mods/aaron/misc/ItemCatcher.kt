package dev.aaronhowser.mods.aaron.misc

import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent

@EventBusSubscriber(
	modid = AaronLib.MOD_ID
)
object ItemCatcher {

	private var isCatchingDrops: Boolean = false
	private val caughtItemEntities: MutableList<ItemEntity> = mutableListOf()

	@JvmStatic
	fun startCatchingItems() {
		isCatchingDrops = true
	}

	@JvmStatic
	fun getCaughtItemEntities(): List<ItemEntity> {
		val entities = caughtItemEntities.toList()
		caughtItemEntities.clear()
		return entities
	}

	@JvmStatic
	fun getCaughtItemStacks(): List<ItemStack> {
		val stacks = getCaughtItemEntities().map(ItemEntity::getItem)
		return stacks
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onEntityJoinLevel(event: EntityJoinLevelEvent) {
		if (!this.isCatchingDrops || event.isCanceled) return

		val entity = event.entity as? ItemEntity ?: return
		caughtItemEntities.add(entity)
	}

}
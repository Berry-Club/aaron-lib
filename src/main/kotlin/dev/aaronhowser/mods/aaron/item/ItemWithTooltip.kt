package dev.aaronhowser.mods.aaron.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ItemWithTooltip(
	properties: Properties,
	private val tooltipProvider: (ItemStack) -> List<Component>
) : Item(properties) {

	constructor(properties: Properties, tooltips: List<Component>) : this(properties, { tooltips })
	constructor(properties: Properties, tooltip: Component) : this(properties, { listOf(tooltip) })

	override fun appendHoverText(
		stack: ItemStack,
		level: Level?,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		tooltipComponents.addAll(tooltipProvider.invoke(stack))
	}

}
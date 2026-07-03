package dev.aaronhowser.mods.aaron.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

class ItemWithTooltip(
	properties: Properties,
	private val tooltipProvider: (ItemStack) -> List<Component>
) : Item(properties) {

	constructor(properties: Properties, tooltips: List<Component>) : this(properties, { tooltips })
	constructor(properties: Properties, tooltip: Component) : this(properties, { listOf(tooltip) })

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		tooltipProvider.invoke(stack).forEach(tooltipComponents::accept)
	}

}

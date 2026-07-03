package dev.aaronhowser.mods.aaron.container

import net.minecraft.world.Container
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.TransactionContext

open class ExtractOnlyResourceHandler(
	handler: ResourceHandler<ItemResource>
) : DelegatingResourceHandler<ItemResource>(handler) {

	constructor(container: Container) : this(VanillaContainerWrapper.of(container))

	override fun insert(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
		return 0
	}

	override fun insert(resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
		return 0
	}

}

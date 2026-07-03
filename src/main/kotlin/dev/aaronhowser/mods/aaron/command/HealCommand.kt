package dev.aaronhowser.mods.aaron.command

import com.mojang.brigadier.builder.ArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.server.permissions.Permissions

object HealCommand : AaronCommandHelper {

	private const val TARGET = "target"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("heal") {

			requires { it.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) }

			executes {
				val source = it.source
				val target = source.playerOrException
				heal(source, target)
			}

			thenArgument(TARGET, EntityArgument.entity()) {
				executes {
					val source = it.source
					val target = EntityArgument.getEntity(it, TARGET)
					heal(source, target)
				}
			}

		}
	}

	private fun heal(
		source: CommandSourceStack,
		target: Entity
	): Int {
		if (target !is LivingEntity) {
			source.sendFailure(Component.literal("Target must be a living entity"))
			return 0
		}

		source.sendSuccess(
			{ Component.literal("Healed ${target.name.string}") },
			false
		)

		target.heal(target.maxHealth)
		target.removeAllEffects()

		if (target is Player) {
			target.foodData.eat(100, 100f)
		}

		return 1
	}

}

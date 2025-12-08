package dev.aaronhowser.mods.aaron.command

import com.mojang.brigadier.CommandDispatcher
import dev.aaronhowser.mods.aaron.AaronLib
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object AaronCommands {

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
		dispatcher.register(
			Commands.literal(AaronLib.MOD_ID)
				.then(HealCommand.register())
		)
	}

}
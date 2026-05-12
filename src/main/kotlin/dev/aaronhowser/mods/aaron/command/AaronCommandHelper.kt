package dev.aaronhowser.mods.aaron.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

interface AaronCommandHelper {

	fun noArg(literal: String, executes: (CommandSourceStack) -> Int): LiteralArgumentBuilder<CommandSourceStack> {
		return Commands.literal(literal).executes { executes(it.source) }
	}

	fun literal(
		literal: String,
		block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
	): LiteralArgumentBuilder<CommandSourceStack> {
		val builder = Commands.literal(literal)
		builder.block()
		return builder
	}

	fun <T> argument(
		name: String,
		type: ArgumentType<T>,
		block: RequiredArgumentBuilder<CommandSourceStack, T>.() -> Unit = {}
	): RequiredArgumentBuilder<CommandSourceStack, T> {
		val builder = Commands.argument(name, type)
		builder.block()
		return builder
	}

	fun <T : ArgumentBuilder<CommandSourceStack, T>> T.thenLiteral(
		name: String,
		block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit = {}
	): T {
		return then(literal(name, block)) as T
	}

	fun <T : ArgumentBuilder<CommandSourceStack, T>> T.thenLiteralExecutes(
		name: String,
		executes: (CommandSourceStack) -> Int
	): T {
		return thenLiteral(name) {
			executes { executes(it.source) }
		}
	}

	fun <T : ArgumentBuilder<CommandSourceStack, T>, A> T.thenArgument(
		name: String,
		type: ArgumentType<A>,
		block: RequiredArgumentBuilder<CommandSourceStack, A>.() -> Unit = {}
	): T {
		return then(argument(name, type, block)) as T
	}

}
package dev.aaronhowser.mods.aaron.misc

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import java.net.URI

@Suppress("unused")
object TextExtensions {
	fun Style.withHoverText(component: Component): Style = withHoverEvent(HoverEvent.ShowText(component))
	fun Style.withHoverText(text: String): Style = withHoverText(Component.literal(text))
	fun Style.withClickToRunCommand(command: String): Style = withClickEvent(ClickEvent.RunCommand(command))
	fun Style.withClickToSuggestCommand(command: String): Style = withClickEvent(ClickEvent.SuggestCommand(command))
	fun Style.withClickToOpenUrl(url: String): Style = withClickEvent(ClickEvent.OpenUrl(URI.create(url)))
	fun Style.withClickToCopyToClipboard(text: String): Style = withClickEvent(ClickEvent.CopyToClipboard(text))

	fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args.map { it ?: "null" }.toTypedArray())
	fun String.toGrayComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args.map { it ?: "null" }.toTypedArray()).withStyle(ChatFormatting.GRAY)
}

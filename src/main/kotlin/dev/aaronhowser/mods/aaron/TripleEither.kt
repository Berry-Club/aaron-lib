package dev.aaronhowser.mods.aaron

sealed class TripleEither<A, B, C> {
	data class First<A, B, C>(val value: A) : TripleEither<A, B, C>()
	data class Second<A, B, C>(val value: B) : TripleEither<A, B, C>()
	data class Third<A, B, C>(val value: C) : TripleEither<A, B, C>()
}
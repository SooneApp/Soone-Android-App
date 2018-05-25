package app.soulcramer.soone.common

@Suppress("unused")
val Any?.unit
    get() = Unit

operator fun StringBuilder.plusAssign(string: String) = append(string).unit

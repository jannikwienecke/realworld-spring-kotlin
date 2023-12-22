package de.wienecke.realworld

import java.util.*

class Slug private constructor(val value: String) {
    companion object {
        fun fromString(input: String): Slug {
            validate(input)
            return Slug(input.toSlug())
        }

        private fun validate(input: String) {
            if (input.isBlank()) {
                throw IllegalArgumentException("Slug must not be blank")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Slug

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

//    equals and hashCode

}

fun String.toSlug() = this.lowercase(Locale.getDefault())
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")
    .replace("-+".toRegex(), "-")





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
}

fun String.toSlug() = this.lowercase(Locale.getDefault())
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")
    .replace("-+".toRegex(), "-")





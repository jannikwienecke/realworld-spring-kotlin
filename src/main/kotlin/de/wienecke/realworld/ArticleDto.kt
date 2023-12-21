package de.wienecke.realworld

import jakarta.validation.constraints.Size

data class ArticleDto(
    val id: Long,
    val title: String,
    val description: String,
    val body: String,
    val tags: Set<String>,
    val slug: String,
)

data class RawArticleData(
    val title: String,
    val description: String,
    val body: String,
    val tags: Set<String>
)

data class AddTagDto(
    @field:Size(min = PostTag.minLength, message = "Tag length must be at least ${PostTag.minLength} characters")
    @field:Size(max = PostTag.maxLength, message = "Tag length must be at most ${PostTag.maxLength} characters")
    val tag: String
)
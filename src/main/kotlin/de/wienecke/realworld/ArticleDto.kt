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
    @field:Size(min = PostTitle.minLength, message = "Title length must be at least ${PostTitle.minLength} characters")
    @field:Size(max = PostTitle.maxLength, message = "Title length must be at most ${PostTitle.maxLength} characters")
    val title: String,
    val description: String?,
    @field:Size(min = 1, message = "Body must not be empty")
    val body: String,
    @field:Size(max = 5, message = "You can only add up to 5 tags")
    val tags: Set<String>
)

data class AddTagDto(
    @field:Size(min = PostTag.minLength, message = "Tag length must be at least ${PostTag.minLength} characters")
    @field:Size(max = PostTag.maxLength, message = "Tag length must be at most ${PostTag.maxLength} characters")
    val tag: String
)
package de.wienecke.realworld

import jakarta.persistence.*
import java.time.LocalDateTime

data class ArticleProps(
    val title: PostTitle,
    val description: String,
    val body: String,
    val tags: PostTags,
    val slug: Slug,
    val author: User,
)

@Entity
@Table(name = "articles")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var body: String,

    @Column(unique = true)
    var slugValue: String,

//    // Assuming a User entity exists for the author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @ElementCollection
    @CollectionTable(name = "article_tags", joinColumns = [JoinColumn(name = "article_id")])
    @Column(name = "tag")
    var tags: Set<String> = setOf(),

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    val articleTags: PostTags
        get() = PostTags.fromStringList(tags.toList()).fold(
            { it },
            { throw PropertyValidationException(propertyName = "tags", errorMessage = (it.message ?: "")) }
        )


    var slug: Slug
        get() = Slug.fromString(slugValue)
        set(value) {
            slugValue = value.value
        }

    fun updateSlug(newSlug: String) {
        this.slug = Slug.fromString(newSlug)
    }

    fun addTag(tag: PostTag) {
        val newTags = articleTags.addTag(tag).fold(
            { it },
            { throw PropertyValidationException(propertyName = "tags", errorMessage = (it.message ?: "")) }
        )

        tags = newTags.toStringList()
    }


    companion object {
        fun createArticle(articleProps: ArticleProps): Article {

            val (title, description, body, tags, slug, author) = articleProps

            val article = Article(
                title = title.value,
                description = description,
                body = body,
                tags = tags.toStringList(),
                slugValue = Slug.fromString(title.value).value,
                author = author
            )

            return article
        }
    }


}

fun Article.toDto() = ArticleDto(
    id = id,
    title = title,
    description = description,
    body = body,
    tags = tags,
    slug = slugValue,
)


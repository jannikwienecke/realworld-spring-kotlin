package de.wienecke.realworld.helpers

import de.wienecke.realworld.*

class UserTestHelper {
    companion object {
        fun createSampleUser(): User {
            return User("johnDoe", "John", "Doe")
        }
    }
}

class PostTitleTestHelper {
    companion object {
        fun createSamplePostTitle(title: String): PostTitle {
            return PostTitle.create(PostTitleProps(title)).getOrNull() ?: throw Exception("Invalid title")
        }
    }
}

class PostTagsTestHelper {
    companion object {
        fun createSamplePostTags(
            tags: Set<String>?
        ): PostTags {
            return PostTags.fromStringList(tags?.toList() ?: setOf("tag1", "tag2").toList()).fold(
                { it },
                { throw PropertyValidationException(propertyName = "tags", errorMessage = (it.message ?: "")) }
            )
        }
    }
}

class ArticleTestHelper {
    companion object {
        fun createSampleArticle(
            tags: Set<String>? = null
        ): Article {
            return Article(
                title = PostTitleTestHelper.createSamplePostTitle("my article 1").value,
                description = "a description about my article",
                body = "This is the body of my article",
                author = UserTestHelper.createSampleUser(),
                tags = PostTagsTestHelper.createSamplePostTags(
                    tags = tags
                ).toStringList(),
                slugValue = Slug.fromString(PostTitleTestHelper.createSamplePostTitle("my article 1").value).value
            )
        }
    }
}

// extension function to create a sample article
fun ArticleTestHelper.Companion.createListSampleArticle(): List<Article> {
    val title1 = "This is my first article"
    val title2 = "This is my second article"
    val title3 = "This is my third article"

    val tag1 = "learn"
    val tag2 = "programming"
    val tag3 = "kotlin"
    val tag4 = "java"

    return listOf(
        Article(
            title = PostTitleTestHelper.createSamplePostTitle(title1).value,
            description = "a description about my article $title1",
            body = "This is the body of my article for $title1",
            author = UserTestHelper.createSampleUser(),
            tags = PostTagsTestHelper.createSamplePostTags(
                tags = setOf(tag1, tag3)
            ).toStringList(),
            slugValue = Slug.fromString(PostTitleTestHelper.createSamplePostTitle(title1).value).value
        ),
        Article(
            title = PostTitleTestHelper.createSamplePostTitle(title2).value,
            description = "a description about my article $title2",
            body = "This is the body of my article for $title2",
            author = UserTestHelper.createSampleUser(),
            tags = PostTagsTestHelper.createSamplePostTags(
                tags = setOf(tag2, tag3)
            ).toStringList(),
            slugValue = Slug.fromString(PostTitleTestHelper.createSamplePostTitle(title2).value).value
        ),
        Article(
            title = PostTitleTestHelper.createSamplePostTitle(title3).value,
            description = "a description about my article $title3",
            body = "This is the body of my article for $title3",
            author = UserTestHelper.createSampleUser(),
            tags = PostTagsTestHelper.createSamplePostTags(
                tags = setOf(tag1, tag4)
            ).toStringList(),
            slugValue = Slug.fromString(PostTitleTestHelper.createSamplePostTitle(title3).value).value
        )

    )
}
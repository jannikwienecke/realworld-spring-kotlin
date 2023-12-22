package de.wienecke.realworld

import de.wienecke.realworld.helpers.ArticleTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ArticleTest {

    private lateinit var article: Article
    private lateinit var tag1: PostTag
    private lateinit var tag2: PostTag
    private val tagName = "myTag1"
    private val tagName2 = "myTag2"

    @BeforeEach
    fun setUp() {
        article = ArticleTestHelper.createSampleArticle(
            tags = setOf(
                "oldTag1",
                "oldTag2",
                "oldTag3",
                "oldTag4",
            )
        )
        tag1 = PostTag.create(PostTagProps(value = tagName)).getOrThrow()
        tag2 = PostTag.create(PostTagProps(value = tagName2)).getOrThrow()

    }

    @Test
    fun `add tag to article tags is valid`() {

        article.addTag(tag = tag1)

        // Assert
        println("article.tagss: ${article.tags}")
        assert(article.tags.contains(tagName))
    }

    @Test
    fun `add tag to article tags and throws TooManyTagsException`() {

//        this is the 5th tag
        article.addTag(tag = tag1)

//        expect "Only 5 tags are allowed" to be thrown
        assertThrows<TooManyTagsException> {
//            this is the 6th tag -> throws TooManyTagsException
            article.addTag(tag = tag2)
        }
    }

    @Test
    fun `add the same tag to article tags and throws TagAlreadyExistsException`() {

        val oldTag4 = PostTag.create(PostTagProps(value = "oldTag4")).getOrThrow()

        assertThrows<TagAlreadyExistsException> {
            article.addTag(tag = oldTag4)
        }
    }

}
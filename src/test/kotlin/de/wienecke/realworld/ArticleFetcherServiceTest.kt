package de.wienecke.realworld

import de.wienecke.realworld.helpers.ArticleTestHelper
import de.wienecke.realworld.helpers.createListSampleArticle
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort

class ArticleFetcherServiceTest {

    private val articleRepository = mockk<ArticleRepository>()
    private val articleFetcherService = ArticleFetcherService(articleRepository)

    private lateinit var mockArticles: List<Article>

    @BeforeEach
    fun setUp() {
        mockArticles = ArticleTestHelper.createListSampleArticle()
    }

    @Test
    fun `getAllArticles returns mapped articles`() {

        // setup
        every {
            articleRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        } returns mockArticles

        // Act
        val result = articleFetcherService.getAllArticles()

        // Assert
        assertEquals(mockArticles.map { it.toDto() }, result)

        val firstResult = result.first()
        val firstArticle = mockArticles.first()

        assertEquals(firstArticle.id, firstResult.id)
        assertEquals(firstArticle.title, firstResult.title)
    }

    @Test
    fun `getSingleArticle returns mapped article`() {
        // setup
        val slug = "slug"
        val article = mockArticles.first()
        every {
            articleRepository.findBySlugValue(slug)
        } returns article

        // Act
        val result = articleFetcherService.getSingleArticle(slug)

        // Assert
        assertEquals(article.toDto(), result)
    }

}



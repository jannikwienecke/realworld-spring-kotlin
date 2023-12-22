package de.wienecke.realworld

import de.wienecke.realworld.helpers.ArticleTestHelper
import de.wienecke.realworld.helpers.UserTestHelper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.*

class ArticleRepo : ArticleRepository {

    override fun <S : Article?> save(entity: S & Any): S & Any {
        return entity
    }

    override fun findBySlugValue(slug: String): Article? {
        TODO("Not yet implemented")
    }

    override fun findAll(sort: Sort): MutableIterable<Article> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<Article> {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Article> {
        TODO("Not yet implemented")
    }

    override fun <S : Article?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableIterable<Article> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Article) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<Article>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Optional<Article> {
        TODO("Not yet implemented")
    }

}

class ArticleServiceTest {

    private lateinit var articleRepository: ArticleRepository
    private val userRepository = mockk<UserRepository>()
    private lateinit var articleService: ArticleService

    private lateinit var sampleUser: User
    private lateinit var sampleArticle: Article

    @BeforeEach
    fun setUp() {
        sampleUser = UserTestHelper.createSampleUser()
        sampleArticle = ArticleTestHelper.createSampleArticle().copy(author = sampleUser)


        articleRepository = ArticleRepo()


        every { userRepository.findByLogin(any()) } returns sampleUser

        // Initializing the service with the mocked repositories
        articleService = ArticleService(articleRepository, userRepository)
    }

    @Test
    fun `when provided with valid data, then can create a new article`() {

        println("mockArticle: $sampleArticle")

        val newArticle = articleService.createArticle(
            RawArticleData(
                title = sampleArticle.title,
                description = sampleArticle.description,
                body = sampleArticle.body,
                tags = sampleArticle.tags
            )
        )

        println("newArticle: $newArticle")

//        assert
        assert(newArticle.title == sampleArticle.title)

    }

    @Test
    fun `when provided with invalid tags, then can not create a new article`() {
        // Arrange
        val invalidArticle = sampleArticle.copy(tags = setOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))

        // Act & Assert
        assertThrows<PropertyValidationException> {
            articleService.createArticle(
                RawArticleData(
                    title = invalidArticle.title,
                    description = invalidArticle.description,
                    body = invalidArticle.body,
                    tags = invalidArticle.tags
                )
            )
        }
    }


}



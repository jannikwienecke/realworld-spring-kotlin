package de.wienecke.realworld

import de.wienecke.realworld.helpers.ArticleTestHelper
import de.wienecke.realworld.helpers.UserTestHelper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class JpaTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Test
    fun `can find user by login`() {
        // Arrange
        UserTestHelper.createSampleUser().let {

            entityManager.persist(it)
            entityManager.flush()

            // Act
            val result = userRepository.findByLogin(it.login)

            // Assert
            assert(result?.login == it.login)

        }
    }

    @Test
    fun `can find article by slug`() {
        // Arrange
        val article = ArticleTestHelper.createSampleArticle()

        entityManager.persist(article.author)

        entityManager.persist(article)
        entityManager.flush()

        // Act
        val result = articleRepository.findBySlugValue(article.slug.value)

        // Assert
        assert(result?.slug == article.slug)
    }

}
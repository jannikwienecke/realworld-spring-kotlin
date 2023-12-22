package de.wienecke.realworld

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RealWorldConfiguration {

    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        articleRepository: ArticleRepository
    ): ApplicationRunner {

        return ApplicationRunner {
            println(">> Inserting data...")
            val johnDoe = userRepository.save(User("johnDoe", "John", "Doe"))

            val rawArticleList = listOf(
                RawArticleData(
                    title = "Spring Framework 5.0 goes GA",
                    description = "Learn Spring Boot",
                    body = "This is a tutorial on Spring Boot",
                    tags = setOf("spring", "boot", "tutorial")
                ),
                RawArticleData(
                    title = "How to make Java fast",
                    description = "Learn Java",
                    body = "This is a tutorial on Java",
                    tags = setOf("java", "tutorial")

                ),
                RawArticleData(
                    title = "Kotlin is the new Java",
                    description = "Learn Kotlin",
                    body = "This is a tutorial on Kotlin",
                    tags = setOf("kotlin", "tutorial")
                ),
                RawArticleData(
                    title = "Why you should use Kotlin",
                    description = "Learn Kotlin",
                    body = "This is a tutorial on Kotlin",
                    tags = setOf("kotlin", "tutorial")
                ),
            )

            val articles = rawArticleList.map { rawArticle ->
                Article(
                    title = rawArticle.title,
                    description = rawArticle.description ?: "",
                    author = johnDoe,
                    body = rawArticle.body,
                    tags = rawArticle.tags,
                    slugValue = Slug.fromString(rawArticle.title).value
                )
            }

            articleRepository.saveAll(articles)
        }
    }
}


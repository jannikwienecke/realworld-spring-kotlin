package de.wienecke.realworld

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ArticleFetcherService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    fun getAllArticles(
        sort: Sort = Sort.by(Sort.Direction.DESC, "createdAt")
    ): List<ArticleDto> {
        val articles = articleRepository.findAll(sort)

        return articles.map { it.toDto() }
    }

    fun getSingleArticle(slug: String): ArticleDto {
        val article = articleRepository.findBySlugValue(slug)
        return article?.toDto() ?: throw ArticleNotFoundException(slug)
    }

    fun createArticle(rawArticleData: RawArticleData): ArticleDto {

        val user = userRepository.findByLogin("johnDoe") ?: throw UserNotFoundException("johnDoe")

        val (title, description, body, tags) = rawArticleData

        val postTitleOrFailure = PostTitle.create(PostTitleProps(title))

        val postTitle = postTitleOrFailure.fold(
            { it },
            { throw PropertyValidationException(propertyName = "title", errorMessage = (it.message ?: "")) }
        )


        val postTags = PostTags.fromStringList(tags.toList()).fold(
            { it },
            { throw PropertyValidationException(propertyName = "tags", errorMessage = (it.message ?: "")) }
        )


        Article.createArticle(
            ArticleProps(
                title = postTitle,
                description = description,
                body = body,
                author = user,
                tags = postTags,
                slug = Slug.fromString(postTitle.value)
            )
        ).let { article ->
            val savedArticle = articleRepository.save(article)
            return savedArticle.toDto()
        }

    }
}
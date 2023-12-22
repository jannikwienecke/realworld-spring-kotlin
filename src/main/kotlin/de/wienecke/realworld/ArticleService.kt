package de.wienecke.realworld

import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {

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
                description = description ?: "",
                body = body,
                author = user,
                tags = postTags,
            )
        ).let { article ->
            val savedArticle = articleRepository.save(article)
            return savedArticle.toDto()
        }

    }

    fun addTag(addTagData: AddTagDto, slug: String): Any {
        val (tag) = addTagData

        val article = articleRepository.findBySlugValue(slug) ?: throw ArticleNotFoundException(slug)

        val postTag = PostTag.create(PostTagProps(value = tag)).fold(
            { it },
            { throw PropertyValidationException(propertyName = "tag", errorMessage = (it.message ?: "")) }
        )

        article.addTag(postTag)

        return articleRepository.save(article).toDto()
    }
}
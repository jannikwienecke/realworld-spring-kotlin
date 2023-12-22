package de.wienecke.realworld

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ArticleFetcherService(
    private val articleRepository: ArticleRepository,
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
}
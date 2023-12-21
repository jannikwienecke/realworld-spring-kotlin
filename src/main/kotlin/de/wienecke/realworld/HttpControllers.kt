package de.wienecke.realworld

import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import kotlin.reflect.full.memberProperties

@RestController
@RequestMapping("/api/articles")
@Validated
class ArticleController(
    val articleRepository: ArticleRepository,
    val articleFetcherService: ArticleFetcherService,
    val articleService: ArticleService
) {

    @GetMapping("/")
    fun findAll(
        @RequestParam sort: String?
    ): List<ArticleDto> {
        val sorting = sort?.let { Sort.by(Sort.Direction.DESC, it) } ?: Sort.by(Sort.Direction.DESC, "createdAt")

        return articleFetcherService.getAllArticles(sort = sorting)

    }

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String): ArticleDto {
        if (slug.isBlank()) {
            throw IllegalArgumentException("slug must not be blank")
        }

        return articleFetcherService.getSingleArticle(slug)
    }

    @PostMapping("/")
    fun create(@RequestBody rawArticleData: RawArticleData): ArticleDto {
        RawArticleData::class.memberProperties.forEach { property ->
            val value = property.get(rawArticleData)

            if (value == null) {
                throw IllegalArgumentException("${property.name} must not be null")
            }
        }

        return articleService.createArticle(rawArticleData)
    }

    @PostMapping("/{slug}/add-tag")
    fun addTag(@RequestBody @Valid addTagData: AddTagDto, @PathVariable slug: String): Any {

        if (slug.isBlank()) {
            throw PropertyValidationException(propertyName = "slug", errorMessage = "slug must not be blank")
        }

        return articleService.addTag(addTagData = addTagData, slug = slug)
    }


}

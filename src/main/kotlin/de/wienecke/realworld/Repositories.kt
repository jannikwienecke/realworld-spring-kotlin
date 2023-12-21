package de.wienecke.realworld

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface ArticleRepository : PagingAndSortingRepository<Article, Long>, CrudRepository<Article, Long> {
    fun findBySlugValue(slug: String): Article?
}

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
}
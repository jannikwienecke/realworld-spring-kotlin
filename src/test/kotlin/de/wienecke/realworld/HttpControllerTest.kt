import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.wienecke.realworld.*
import de.wienecke.realworld.helpers.ArticleTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ContextConfiguration(classes = [RealWorldApplication::class])
class HttpControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var articleFetcherService: ArticleFetcherService

    @MockBean
    private lateinit var articleService: ArticleService

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var articleRepository: ArticleRepository

    private lateinit var rawArticleData: RawArticleData
    private lateinit var article: Article

    @BeforeEach
    fun setUp() {

        article = ArticleTestHelper.createSampleArticle()
        rawArticleData = RawArticleData(
            title = article.title,
            description = article.description,
            body = article.body,
            tags = article.tags
        )
    }

    @Test
    fun `create article with valid data`() {
        // Arrange
        val articleDto = article.toDto()
//
        Mockito.`when`(
            articleService.createArticle(
                rawArticleData
            )
        ).thenReturn(articleDto)

        val objectMapper = jacksonObjectMapper()
        val jsonRawArticle = objectMapper.writeValueAsString(rawArticleData)
        val jsonArticleDto = objectMapper.writeValueAsString(articleDto)

        // Act & Assert
        mockMvc.perform(
            post("/api/articles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRawArticle)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(jsonArticleDto))
    }

    @Test
    fun `create article with invalid title then returns bad request`() {
        // Arrange
        val objectMapper = jacksonObjectMapper()
        val jsonRawArticle = objectMapper.writeValueAsString(rawArticleData.copy(title = "1"))

        // Act & Assert
        mockMvc.perform(
            post("/api/articles/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRawArticle)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().json("""{"errors":{"title":"Title length must be at least ${PostTitle.minLength} characters"}}"""))
    }


}

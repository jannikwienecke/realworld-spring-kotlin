package de.wienecke.realworld

data class PostTitleProps(val value: String)

class PostTitle private constructor(private val props: PostTitleProps) {
    val value: String
        get() = props.value

    companion object {
        const val minLength: Int = 2
        const val maxLength: Int = 85

        fun create(props: PostTitleProps): Result<PostTitle> {
            if (props.value.length < minLength) {
                return Result.failure(IllegalArgumentException("Post title is too short"))
            }
            if (props.value.length > maxLength) {
                return Result.failure(IllegalArgumentException("Post title is too long"))
            }
            return Result.success(PostTitle(props))
        }
    }
}

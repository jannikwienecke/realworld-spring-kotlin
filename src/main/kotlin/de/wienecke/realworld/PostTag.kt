package de.wienecke.realworld

data class PostTagProps(val value: String)

class PostTag private constructor(private val props: PostTagProps) {
    val value: String
        get() = props.value

    companion object {
        const val minLength: Int = 3
        const val maxLength: Int = 20

        fun create(props: PostTagProps): Result<PostTag> {
            if (props.value.length < minLength) {
                return Result.failure(IllegalArgumentException("Tag is too short"))
            }
            if (props.value.length > maxLength) {
                return Result.failure(IllegalArgumentException("Tag is too long"))
            }
            return Result.success(PostTag(props))
        }
    }
}

data class PostTagsProps(val tags: Set<PostTag>)

class PostTags private constructor(private val props: PostTagsProps) {
    val tags: Set<PostTag>
        get() = props.tags

    fun toStringList(): Set<String> {
        return tags.map { it.value }.toSet()
    }

    fun addTag(tag: PostTag): Result<PostTags> {
        if (tags.size >= maxTags) {
            return Result.failure(TooManyTagsException("Only $maxTags tags are allowed"))
        }

        val isExisting = validateIfExistingTag(tag)

        if (isExisting.isFailure) {
            return Result.failure(isExisting.exceptionOrNull()!!)
        }

        val newTags = tags.toMutableSet()
        newTags.add(tag)

        return Result.success(PostTags(PostTagsProps(newTags)))

    }

    private fun validateIfExistingTag(tag: PostTag): Result<PostTags> {
        tags.find { it.value == tag.value }?.let {
            return Result.failure(TagAlreadyExistsException("Tag ${tag.value} already exists"))
        }

        return Result.success(this)
    }

    companion object {
        const val maxTags: Int = 5

        fun create(props: PostTagsProps): Result<PostTags> {
            if (props.tags.size > maxTags) {
                return Result.failure(IllegalArgumentException("Too many tags"))
            }
            return Result.success(PostTags(props))
        }

        fun fromStringList(tagStrings: List<String>): Result<PostTags> {
            val tagResults = tagStrings.map { PostTag.create(PostTagProps(it)) }

            val failedTagResult = tagResults.firstOrNull { it.isFailure }

            if (failedTagResult != null) {
                return Result.failure(failedTagResult.exceptionOrNull()!!)
            }

            val tags = tagResults.mapNotNull { it.getOrNull() }.toSet()
            return create(PostTagsProps(tags))
        }


    }
}

class TooManyTagsException(message: String) : Exception(message)
class TagAlreadyExistsException(message: String) : Exception(message)
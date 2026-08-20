package ru.netology.nmedia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Comment(
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String? = null,
    val content: String,
    val published: Instant,
    val likedByMe: Boolean,
    val likes: Int = 0,
) {
    @JsonCreator
    constructor(
        @JsonProperty("id") id: Long?,
        @JsonProperty("postId") postId: Long?,
        @JsonProperty("authorId") authorId: Long?,
        @JsonProperty("author") author: String?,
        @JsonProperty("authorAvatar") authorAvatar: String?,
        @JsonProperty("content") content: String,
        @JsonProperty("published") published: Instant?,
        @JsonProperty("likedByMe") likedByMe: Boolean?,
        @JsonProperty("likes") likes: Int?,
    ) : this(
        id = id ?: 0,
        postId = postId ?: 0,
        author = author ?: "",
        authorAvatar = authorAvatar,
        content = content,
        published = published ?: Instant.now(),
        likedByMe = likedByMe ?: false,
        likes = likes ?: 0,
        authorId = authorId ?: 0,
    )
}

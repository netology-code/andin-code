package ru.netology.nmedia.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Instant,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val authorAvatar: String? = null,
) {
    @JsonCreator
    constructor(
        @JsonProperty("id") id: Long?,
        @JsonProperty("author") author: String?,
        @JsonProperty("content") content: String,
        @JsonProperty("published") published: Instant?,
        @JsonProperty("likedByMe") likedByMe: Boolean?,
        @JsonProperty("likes") likes: Int?,
        @JsonProperty("authorAvatar") authorAvatar: String?,
    ) : this(
        id = id ?: 0,
        author = author ?: "",
        content = content,
        published = published ?: Instant.now(),
        likedByMe = likedByMe ?: false,
        authorAvatar = authorAvatar,
        likes = likes ?: 0,
    )
}
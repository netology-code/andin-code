package ru.netology.nmedia.dto

import java.time.Instant

data class Comment(
    val id: Long = 0,
    val postId: Long = 0,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String,
    val published: Instant = Instant.now(),
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)

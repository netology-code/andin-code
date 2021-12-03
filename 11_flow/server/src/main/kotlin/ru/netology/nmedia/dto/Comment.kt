package ru.netology.nmedia.dto

data class Comment(
    val id: Long,
    val postId: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
)

package ru.netology.nmedia.dto

data class Post(
    val id: Long = 0L,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false ,
    var likes: Int = 0,
)


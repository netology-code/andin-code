package ru.netology.nmedia.dto

import androidx.room.Embedded

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    @Embedded
    var attachment: Attachment? = null
){
    data class Attachment(
        val url: String,
        val description: String,
        val type: String
    )
}


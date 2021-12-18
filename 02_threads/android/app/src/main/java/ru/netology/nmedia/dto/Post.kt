package ru.netology.nmedia.dto

import ru.netology.nmedia.enumartion.AttachmentType

data class Post(
    val id: Long = 0L,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    var attachment: Attachment? = null,
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)


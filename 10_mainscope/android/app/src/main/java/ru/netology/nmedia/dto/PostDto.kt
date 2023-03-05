package ru.netology.nmedia.dto

import ru.netology.nmedia.model.Post

data class PostDto(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
) {
    companion object {
        fun fromModel(post: Post): PostDto = with(post) {
            PostDto(
                id = serverId ?: 0,
                author = author,
                authorAvatar = authorAvatar,
                content = content,
                published = published,
                likedByMe = likedByMe,
                likes = likes,
            )
        }
    }
}

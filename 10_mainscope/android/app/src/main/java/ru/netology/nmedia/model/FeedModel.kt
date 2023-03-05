package ru.netology.nmedia.model

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)

package ru.netology.nmedia.model

/**
 * Пришлось создать отдельную модель, чтобы состояние загрузки показывать.
 * @param state - состояние отправки на сервер. Этого нет ни на сервере, ни в БД
 */
data class Post(
    val localId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val serverId: Long? = null,
    val state: LoadingState = LoadingState(),
)

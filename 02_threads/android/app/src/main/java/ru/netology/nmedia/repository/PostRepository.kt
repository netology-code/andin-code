package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun likeById(id: Long, callback: GetCallback)
    fun unLikeById(id: Long, callback: GetCallback)
    fun save(post: Post)
    fun removeById(id: Long)
    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface GetCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}

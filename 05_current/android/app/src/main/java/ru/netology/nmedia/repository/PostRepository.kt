package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    fun saveAsync(post: Post, callback: SaveCallback)
    fun removeByIdAsync(id: Long, callback: RemoveCallback)
    fun likeByIdAsync(id: Long, callback: LikeCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface SaveCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }

    interface RemoveCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }

    interface LikeCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }
}

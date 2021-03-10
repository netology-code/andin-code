package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun save(post: Post, callback: Callback<Post>)
    fun removeById(id: Long, callback: Callback<Unit>)
    fun likeById(id: Long, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}

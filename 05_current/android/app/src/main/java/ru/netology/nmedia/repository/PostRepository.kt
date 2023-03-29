package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetCallback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: GetCallback<Long>)
    fun dislikeByIdAsync(id: Long, callback: GetCallback<Long>)
    fun removeByIdAsync(id: Long, callback: GetCallback<Unit>)
    fun saveAsync(post: Post, callback: GetCallback<Post>)
    fun share(id: Long)
    fun getPostByIdAsync(id: Long, callback: GetCallback<Post>)
    interface GetCallback<T> {
        fun onSuccess(posts: T)
        fun onError(e: Exception)
    }
}
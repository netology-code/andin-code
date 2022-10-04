package ru.netology.nmedia.repository

import okhttp3.Callback
import ru.netology.nmedia.dto.Post

interface PostRepository {


    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun dislikeByIdAsync(id: Long, callback: Callback<Post>)
    fun shareById(id: Long)
    fun saveAsync(post: Post, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Boolean>)

    interface Callback<T> {
        fun onSuccess(post: T)
        fun onError(e: Exception)
    }
}

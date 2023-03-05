package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.model.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun saveLocal(post: Post): Post
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
}

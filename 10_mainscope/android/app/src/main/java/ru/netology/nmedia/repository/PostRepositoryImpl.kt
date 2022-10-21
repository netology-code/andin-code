package ru.netology.nmedia.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.time.OffsetDateTime

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toDto)

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun save(post: Post) {
        try {
            dao.insert(PostEntity.fromDto(post))
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }else{
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromDto(body.copy(isSendToServer = true)))
            }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val post = data.value?.last {
                it.id == id
            }
            dao.removeById(id)
            val response = PostsApi.service.removeById(id)
            if (!response.isSuccessful) {
                dao.insert(PostEntity.fromDto(post!!))
                throw ApiError(response.code(), response.message())
            }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        val post = data.value?.last {
            it.id == id
        }
        try {
            if (post?.likedByMe == false) {
                dao.insert(PostEntity.fromDto(post).copy(likedByMe = true, likes = post.likes + 1))
                val response = PostsApi.service.likeById(id)
                if (!response.isSuccessful) {
                    dao.insert(PostEntity.fromDto(post))
                    throw ApiError(response.code(), response.message())
                }
            } else {
                dao.insert(
                    PostEntity.fromDto(post!!).copy(likedByMe = false, likes = post.likes - 1)
                )
                val response = PostsApi.service.dislikeById(id)
                if (!response.isSuccessful) {
                    dao.insert(PostEntity.fromDto(post))
                    throw ApiError(response.code(), response.message())
                }
            }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


}

package ru.netology.nmedia.repository

import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.PostDto
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.entity.toModel
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.model.Post

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    // Сначала локальные, потом сохранённые
    override val data = dao.observeLocal().combine(dao.observeServer()) { local, server ->
        local + server
    }
        .map(List<PostEntity>::toModel)
        .asLiveData()

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            // Чтобы избежать дублей, сначала прочитаем локальные посты
            val localPosts = dao.getAll()
            val serverIdsToPosts = localPosts.groupBy { it.serverId }
            dao.insert(
                body.toEntity().map {
                    // Найдём существующий пост по serverId, если есть
                    val localPost = serverIdsToPosts[it.serverId]?.firstOrNull()
                    it.copy(localId = localPost?.localId ?: 0L)
                }
            )
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(PostDto.fromModel(post))
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body).copy(localId = post.localId))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveLocal(post: Post): Post =
        dao.insert(PostEntity.fromModel(post))
            .let { dao.getById(it) }
            ?.toModel()
            .let(::requireNotNull)


    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun likeById(id: Long) {
        TODO("Not yet implemented")
    }
}

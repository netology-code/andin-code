package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException

class PostRepositoryImpl : PostRepository {
    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        // TODO("Not yet implemented")
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        // TODO("Not yet implemented")
    }

    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        // TODO("Not yet implemented")
    }

}

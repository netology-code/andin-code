package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val postToken = object : TypeToken<Post>() {}

    companion object {
        private const val BASE_URL = "http://192.168.0.103:9999"
        private val jsonType = "application/json".toMediaType()
    }
    override fun getAllAsync(callback: PostRepository.GetCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string()
                            ?: throw java.lang.RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }

                }
            })

    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.GetCallback<Long>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(id).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(id)
                }
            })
    }

    override fun dislikeByIdAsync(id: Long, callback: PostRepository.GetCallback<Long>) {
        val request: Request = Request.Builder()
            .delete(gson.toJson(id).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(id)
            }

        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.GetCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string()
                            ?: throw java.lang.RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, postToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun share(id: Long) {}

    override fun getPostByIdAsync(id: Long, callback: PostRepository.GetCallback<Post>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string()
                            ?: throw java.lang.RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, postToken.type))

                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })

    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.GetCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }
            })
    }
}

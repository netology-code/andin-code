package ru.netology.pools

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.pools.dto.Post
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()
    val gson = Gson()
    val typeToken = object : TypeToken<List<Post>>() {}
    val baseUrl = "http://127.0.0.1:9999"

    val postsRequest = Request.Builder().url("$baseUrl/api/posts").build()
    val posts: List<Post> = client.newCall(postsRequest)
        .execute()
        .let { it.body?.string() ?: throw RuntimeException("body is null") }
        .let { gson.fromJson(it, typeToken.type) }

    val avatarsLoadTasks = posts.asSequence()
        .map { it.authorAvatar }
        .distinct()
        .map { it to Request.Builder().url("$baseUrl/avatars/$it").build() }
        .map { (url, request) ->
            Callable {
                println("executed in ${Thread.currentThread().name}")
                client.newCall(request)
                    .execute()
                    .body?.use {
                        Files.copy(it.byteStream(), Paths.get(url), StandardCopyOption.REPLACE_EXISTING)
                        url
                    }
            }
        }.toMutableList()

    val newFixedThreadPool = Executors.newFixedThreadPool(64)
    val list = newFixedThreadPool.invokeAll(avatarsLoadTasks)
    for (future in list) {
        println(future.get())
    }
    newFixedThreadPool.shutdown()
}


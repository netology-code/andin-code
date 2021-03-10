package ru.netology.coroutines

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.coroutines.dto.Comment
import ru.netology.coroutines.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Cancellation sample
private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() = runBlocking {
    val job = with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val postsTypeToken = object : TypeToken<List<Post>>() {}
                val posts: List<Post> = client.apiCall(
                    "$BASE_URL/api/slow/posts"
                ).let { response ->
                        if (!response.isSuccessful) {
                            response.close()
                            throw RuntimeException(response.message)
                        }
                        val body = response.body ?: throw RuntimeException("response body is null")
                        gson.fromJson(body.string(), postsTypeToken.type)
                    }
                val id = posts.last().id
                launch {
                    try {
                        val commentsTypeToken = object : TypeToken<List<Comment>>() {}
                        val comments: List<Comment> = client.apiCall(
                            "$BASE_URL/api/slow/posts/$id/comments"
                        ).let { response ->
                                if (!response.isSuccessful) {
                                    response.close()
                                    throw RuntimeException(response.message)
                                }
                                val body = response.body ?: throw RuntimeException("response body is null")
                                gson.fromJson(body.string(), commentsTypeToken.type)
                            }
                        println(comments)
                        println(this)
                        println("$isActive in child job")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    delay(6000)
    println(job.children.joinToString(", "))
    job.cancel()
    println(job.children.joinToString(", "))
    job.join()
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    // TODO: see suspendCancellableCoroutine for cancellation support at this level
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}

/* // Sample with launch vs async (launch)
private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() = runBlocking {
    val job = with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val postsTypeToken = object : TypeToken<List<Post>>() {}
                val posts: List<Post> = client.apiCall(
                    "$BASE_URL/api/slow/posts"
                ).let { response ->
                        if (!response.isSuccessful) {
                            response.close()
                            throw RuntimeException(response.message)
                        }
                        val body = response.body ?: throw RuntimeException("response body is null")
                        gson.fromJson(body.string(), postsTypeToken.type)
                    }
                val id = posts.last().id
                println("before launch")
                launch {
                    try {
                        val commentsTypeToken = object : TypeToken<List<Comment>>() {}
                        val comments: List<Comment> = client.apiCall(
                            "$BASE_URL/api/slow/posts/$id/comments"
                        ).let { response ->
                                if (!response.isSuccessful) {
                                    response.close()
                                    throw RuntimeException(response.message)
                                }
                                val body = response.body ?: throw RuntimeException("response body is null")
                                gson.fromJson(body.string(), commentsTypeToken.type)
                            }
                        println(comments)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                println("after launch")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    job.join()
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}
*/

/* // Sample with launch vs async (async)
private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() = runBlocking {
    val job = with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val postsTypeToken = object : TypeToken<List<Post>>() {}
                val posts: List<Post> = client.apiCall(
                    "$BASE_URL/api/slow/posts"
                ).let { response ->
                        if (!response.isSuccessful) {
                            response.close()
                            throw RuntimeException(response.message)
                        }
                        val body = response.body ?: throw RuntimeException("response body is null")
                        gson.fromJson(body.string(), postsTypeToken.type)
                    }
                val id = posts.last().id
                println("before launch")
                async {
                    try {
                        val commentsTypeToken = object : TypeToken<List<Comment>>() {}
                        val comments: List<Comment> = client.apiCall(
                            "$BASE_URL/api/slow/posts/$id/comments"
                        ).let { response ->
                                if (!response.isSuccessful) {
                                    response.close()
                                    throw RuntimeException(response.message)
                                }
                                val body = response.body ?: throw RuntimeException("response body is null")
                                gson.fromJson(body.string(), commentsTypeToken.type)
                            }
                        println(comments)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.await()
                println("after launch")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    job.join()
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}
*/

/* // withContext Sample
private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() = runBlocking {
    val job = with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val postsTypeToken = object : TypeToken<List<Post>>() {}
                val posts: List<Post> = client.apiCall(
                    "$BASE_URL/api/slow/posts"
                ).let { response ->
                    if (!response.isSuccessful) {
                        response.close()
                        throw RuntimeException(response.message)
                    }
                    val body = response.body ?: throw RuntimeException("response body is null")
                    gson.fromJson(body.string(), postsTypeToken.type)
                }
                val id = posts.last().id
                println("before launch")
                withContext(Dispatchers.Default) {
                    try {
                        val commentsTypeToken = object : TypeToken<List<Comment>>() {}
                        val comments: List<Comment> = client.apiCall(
                            "$BASE_URL/api/slow/posts/$id/comments"
                        ).let { response ->
                            if (!response.isSuccessful) {
                                response.close()
                                throw RuntimeException(response.message)
                            }
                            val body = response.body ?: throw RuntimeException("response body is null")
                            gson.fromJson(body.string(), commentsTypeToken.type)
                        }
                        println(comments)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                println("after launch")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    job.join()
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}
*/

/* // Exception with Thread
fun main() {
    thread {
        println("before exception")
        throw Exception("something bad happened")
    }
    Thread.sleep(1000)
}
*/

/* // Exception Sample
fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            println("before exception")
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
*/

/* // Exception Sample
fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        async {
            println("before exception")
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
*/

/* // Exception Handler Sample
fun main() {
    val handler = CoroutineExceptionHandler { context, throwable ->
        println(throwable)
    }
    with(CoroutineScope(EmptyCoroutineContext) + handler) {
        launch {
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
*/

/* // Exception Job State Sample
fun main() {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        println("before exception")
        throw Exception("something bad happened")
    }
    Thread.sleep(1000)
    println(job)
}
*/


/* // Exception Job State Sample with async/await
fun main() {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        try {
            val deferred = async {
                println("before exception")
                throw Exception("something bad happened")
            }
            println("just sample")
            val result = deferred.await()
        } catch (e: Exception) {
            println("caught")
        }
    }
    Thread.sleep(1000)
    println(job.isCancelled) // true
}
*/

/* Multiple CoroutineScope sample
fun main() {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext).launch {
            try {
                async {
                    println("before exception")
                    throw Exception("something bad happened")
                }.await()
            } catch (e: Exception) {
                println("caught")
            }
        }
    }
    Thread.sleep(1000)
    println(job.isCancelled) // false
}
*/

/* // coroutineScope sample
fun main() {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                launch {
                    throw Exception("something bad happened")
                }
                launch {
                    delay(100)
                    println("will never be executed")
                }
            }
        } catch (e: Exception) {
            println(e.message) // something bad happened
        }
    }
    Thread.sleep(1000)
    println(job.isCancelled) // false
}
*/

/* withContext sample
fun main() {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        try {
            withContext(Dispatchers.Default) {
                launch {
                    throw Exception("something bad happened")
                }
                launch {
                    delay(100)
                    println("will never be executed")
                }
            }
        } catch (e: Exception) {
            println(e.message) // something bad happened
        }
    }
    Thread.sleep(1000)
    println(job.isCancelled) // false
}
*/

/*
fun main() {
    with(CoroutineScope(EmptyCoroutineContext + SupervisorJob())) {
        launch {
            println("first child throw exception")
            throw Exception("something bad happened")
        }
        launch {
            delay(100)
            println("second child work")
        }
    }
    Thread.sleep(1000)
}
*/

/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        val job = supervisorScope {
            launch {
                println("first child throw exception")
                throw Exception("something bad happened")
            }
            launch {
                delay(100)
                println("second child work")
            }
        }
        // work after all
        println(job)
    }
    Thread.sleep(1000)
}
*/

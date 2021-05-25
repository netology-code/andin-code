package ru.netology.nmedia.repository

import com.google.gson.JsonObject
import org.json.JSONObject
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.model.JsonLogInModel
import java.io.IOException

class LogInRepositoryImpl : LogInRepository {

    override suspend fun onSignIn(login: String, password: String): JsonLogInModel {
        try {
            val response = PostsApi.service.onSignIn(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            print(e.message)
            throw UnknownError
        }
    }
}
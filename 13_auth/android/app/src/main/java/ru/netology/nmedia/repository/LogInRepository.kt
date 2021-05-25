package ru.netology.nmedia.repository

import com.google.gson.JsonObject
import org.json.JSONObject
import ru.netology.nmedia.model.JsonLogInModel

interface LogInRepository {
    suspend fun onSignIn(login: String, password: String): JsonLogInModel
}
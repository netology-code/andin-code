package ru.netology.nmedia.model

import com.google.gson.annotations.SerializedName

data class JsonLogInModel(
    @SerializedName("id")
    var userId: Long?,
    @SerializedName("token")
    var token: String?
) {
}
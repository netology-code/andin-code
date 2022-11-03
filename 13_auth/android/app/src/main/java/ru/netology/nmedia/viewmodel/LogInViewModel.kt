package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class LogInViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())
    private var state = false
        set(value) {
            field = value
            authState.value = field
        }
    val authState = MutableLiveData(state)


    fun userAuth(login: String, pass: String) {
        viewModelScope.launch {
            val auth = repository.updateUser(login, pass)
            AppAuth.getInstance().setAuth(
                auth.id,
                token = auth.token!!
            )
            state = true
        }
    }

    fun userRegister(login: String, pass: String, name: String) {
        viewModelScope.launch {
            val auth = repository.registerUser(login, pass, name)
            AppAuth.getInstance().setAuth(
                auth.id,
                token = auth.token!!
            )
            state = true
        }
    }
}
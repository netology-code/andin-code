package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.repository.LogInRepository
import ru.netology.nmedia.repository.LogInRepositoryImpl

class LogInViewModel: ViewModel() {
    private val repository: LogInRepository = LogInRepositoryImpl()

    private val _isSignedIn = MutableLiveData(false)
    val isSignedIn : LiveData<Boolean>
    get() = _isSignedIn

    fun invalidateSignedInState() {
        _isSignedIn.value = false
    }

    fun onSignIn(login:String, password: String){
        viewModelScope.launch {
            val idAndToken = repository.onSignIn(login, password)
            val id = idAndToken.userId ?: 0L
            val token = idAndToken.token ?: "N/A"
            AppAuth.getInstance().setAuth(id= id, token = token)
            _isSignedIn.value = true
        }
    }


}
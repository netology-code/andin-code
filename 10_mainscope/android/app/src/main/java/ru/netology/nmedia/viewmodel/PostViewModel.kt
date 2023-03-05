package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.LoadingState
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    localId = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    private val postStates = MutableLiveData<Map<Long, LoadingState>>(mapOf())

    val data: LiveData<FeedModel> = postStates.asFlow()
        .combine(repository.data.asFlow()) { states, posts ->
            FeedModel(
                posts.map { post ->
                    post.copy(
                        // Если пост вдруг уже загружен, убираем индикацию
                        state = post.serverId?.let { LoadingState() }
                            ?: states[post.localId]
                            ?: post.state
                    )
                },
                posts.isEmpty(),
            )
        }.asLiveData()

    private val _dataState = MutableLiveData<LoadingState>()
    val dataState: LiveData<LoadingState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = LoadingState(loading = true)
            repository.getAll()
            _dataState.value = LoadingState()
        } catch (e: Exception) {
            _dataState.value = LoadingState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = LoadingState(refreshing = true)
            repository.getAll()
            _dataState.value = LoadingState()
        } catch (e: Exception) {
            _dataState.value = LoadingState(error = true)
        }
    }

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                val localPost = repository.saveLocal(it)
                val currentMutableState = postStates.value.orEmpty()
                    .toMutableMap()
                try {
                    postStates.value = currentMutableState.apply {
                        put(localPost.localId, LoadingState(loading = true))
                    }

                    // Сразу выходим, дальше наша shared view model догрузит
                    _postCreated.value = Unit
                    edited.value = empty

                    repository.save(localPost)

                    postStates.value = currentMutableState.apply {
                        remove(localPost.localId)
                    }
                } catch (e: Exception) {
                    postStates.value = currentMutableState
                        .apply {
                            put(localPost.localId, LoadingState(error = true))
                        }
                }
            }
        }
    }

    fun retrySaving(post: Post) {
        viewModelScope.launch {
            val currentMutableState = postStates.value.orEmpty()
                .toMutableMap()
            try {
                postStates.value = currentMutableState.apply {
                    put(post.localId, LoadingState(loading = true))
                }

                repository.save(post)

                postStates.value = currentMutableState
                    .apply {
                        remove(post.localId)
                    }
            } catch (e: Exception) {
                postStates.value = currentMutableState.apply {
                    put(post.localId, LoadingState(error = true))
                }
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        TODO()
    }

    fun removeById(id: Long) {
        TODO()
    }
}

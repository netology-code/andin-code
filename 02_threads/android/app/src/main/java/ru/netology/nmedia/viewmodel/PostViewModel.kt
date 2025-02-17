package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    authorAvatar = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    private val _singleError = SingleLiveEvent<Unit>()
    val singleError: LiveData<Unit>
        get() = _singleError

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
                _singleError.postValue(Unit)
                _data.value = FeedModel(error = true)
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _singleError.postValue(Unit)
                    _data.postValue(FeedModel(error = true))
                }
            })

        }
        edited.value = empty
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
        val likedByMe = _data.value?.posts?.find { it.id == id }?.likedByMe ?: return
        if (!likedByMe) {
            repository.likeById(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    _data.postValue(
                        _data.value?.copy(
                            posts = _data.value?.posts.orEmpty().map {
                                if (it.id == id) post else it
                            },
                            loading = false
                        )
                    )
                }

                override fun onError(e: Exception) {
                    _singleError.postValue(Unit)
                    _data.postValue(FeedModel(error = true))
                }
            })
        } else {
            repository.unLikeById(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    _data.postValue(
                        _data.value?.copy(
                            posts = _data.value?.posts.orEmpty().map {
                                if (it.id == id) post else it
                            },
                            loading = false
                        )
                    )
                }

                override fun onError(e: Exception) {
                    _singleError.postValue(Unit)
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun removeById(id: Long) {
        // Оптимистичная модель
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        try {
            repository.removeById(id, object : PostRepository.Callback<Unit> {})
        } catch (e: IOException) {
            _singleError.postValue(Unit)
            _data.postValue(_data.value?.copy(posts = old))
        }
    }
}

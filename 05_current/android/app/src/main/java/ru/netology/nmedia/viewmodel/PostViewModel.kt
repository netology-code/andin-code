package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepository.GetCallback
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = ""
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

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : GetCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : GetCallback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreated.postValue(Unit)

                }
                override fun onError(e: Exception) {
                    edited.value = empty
                }
            })
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
        repository.likeByIdAsync(id, object : GetCallback<Long> {
            override fun onSuccess(posts: Long) {
                ///nen
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == posts) it.copy(
                                likedByMe = true,
                                likes = it.likes + 1
                            ) else it
                        }
                    )
                )
            }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
        })
    }

    fun dislikedById(id: Long) {
        repository.dislikeByIdAsync(id, object : GetCallback<Long> {
            override fun onSuccess(posts: Long) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id == id) it.copy(
                                likedByMe = false,
                                likes = it.likes - 1
                            ) else it
                        }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object : GetCallback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    ))
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
}

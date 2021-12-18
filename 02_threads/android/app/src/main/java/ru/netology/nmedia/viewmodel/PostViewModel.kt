package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
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
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.RepositoryCallback<List<Post>> {
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(id: Long) {
        repository.likeByIdAsync(id, object : PostRepository.RepositoryCallback<Post> {
            override fun onSuccess(value: Post) {
                _data.value?.posts?.replacePost(value, { it.id == id })
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun unLikeById(id: Long) {
        repository.unLikeByIdAsync(id, object : PostRepository.RepositoryCallback<Post> {
            override fun onSuccess(value: Post) {
                _data.value?.posts?.replacePost(value, { it.id == id })
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.RepositoryCallback<Post> {
                override fun onSuccess(value: Post) {
                    _data.value?.posts.orEmpty()
                        .plus(value)
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.value = _data.value?.copy(posts = _data.value?.posts.orEmpty()
            .filter { it.id != id }
        )

        repository.removeByIdAsync(id, object : PostRepository.RepositoryCallback<Post> {
            override fun onSuccess(value: Post) {
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }

        })
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

    fun <T> List<T>.replacePost(newPost: T, block: (T) -> Boolean): List<T> {
        return map {
            if (block(it)) newPost else it
        }
    }
}

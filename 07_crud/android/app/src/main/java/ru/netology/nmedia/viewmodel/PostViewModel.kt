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

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
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
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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
                _data.value = FeedModel(error = true)

            }
        })
    }

    fun save() {
        edited.value?.let { post ->
            repository.save(post, object : PostRepository.Callback<Post> {
                override fun onSuccess(result: Post) {

                    val currentData = _data.value ?: return
                    val updatedPosts = currentData.posts?.toMutableList() ?: mutableListOf()


                    val index = updatedPosts.indexOfFirst { it.id == result.id }
                    if (index != -1) {

                        updatedPosts[index] = result
                    } else {

                        updatedPosts.add(0, result)
                    }

                    _data.value = currentData.copy(posts = updatedPosts)
                    _postCreated.value = Unit
                }

                override fun onError(e: Exception) {
                    _errorMessage.value = "Ошибка при сохранении поста"
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
        repository.likeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(result: Post) {
                val currentData = _data.value ?: return
                val updatedPosts = currentData.posts?.map { post ->
                    if (post.id == result.id) {
                        post.copy(
                            likes = post.likes + 1,
                            likedByMe = true
                        )
                    } else {
                        post
                    }
                }
                _data.value = currentData.copy(posts = updatedPosts ?: emptyList())
            }

            override fun onError(e: Exception) {

                _errorMessage.value = "Ошибка при лайке поста"
            }
        })
    }

    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(result: Unit) {
                val currentData = _data.value ?: return
                val updatedPosts = currentData.posts?.filter { it.id != id }
                _data.value = currentData.copy(posts = updatedPosts ?: emptyList())
            }

            override fun onError(e: Exception) {
                _errorMessage.value = "Ошибка при удалении поста"
            }
        })
    }
}
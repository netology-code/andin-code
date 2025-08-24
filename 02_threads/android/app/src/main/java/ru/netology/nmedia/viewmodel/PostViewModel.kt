package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    sharedByMe = false
)
class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedState())
    val data: LiveData<FeedState> = _data

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated

    init {
        load()
    }

    fun load() {
        _data.postValue(FeedState(loading = true))

        repository.getAllAsync(
            object : PostRepository.PostCallback<List<Post>> {
                override fun onSuccess(result: List<Post>) {
                    _data.value = (FeedState(posts = result, empty = result.isEmpty()))
                }

                override fun onError(error: Throwable) {
                    _data.value = (FeedState(error = true))
                }

            })
    }

    val edited = MutableLiveData(empty)

    fun likeByID(id: Long) {
        val currentState = _data.value ?: return
        val posts = currentState.posts


        val post = posts.find { it.id == id } ?: return
        val likedByMe = post.likedByMe

        repository.likeByID(id, likedByMe, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(result: Post) {
                val refreshState = _data.value ?: return
                val updatedPosts = refreshState.posts.map {
                    if (it.id == result.id) result else it
                }
                _data.postValue(refreshState.copy(posts = updatedPosts))
            }


            override fun onError(error: Throwable) {
                _data.value
            }

        })
    }
    fun shareByID(id: Long) = repository.shareByID(id)


    fun removeByID(id: Long) {
        val currentState = _data.value ?: return
        _data.postValue(currentState.copy(posts = currentState.posts.filter { it.id != id }))

        repository.removeByID(id, object : PostRepository.PostCallback<Unit> {
            override fun onSuccess(result: Unit) {

            }

            override fun onError(error: Throwable) {
                _data.postValue(currentState)
            }

        })

    }



    fun edit(post: Post){
        edited.value = post
    }
    fun clear() {
        edited.value = empty
    }
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.PostCallback<Post> {
                override fun onSuccess(result: Post) {

                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Throwable) {
                    _data.value

                }
            })
        }
        edited.value = empty
    }

}
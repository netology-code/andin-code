package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

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
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let { data ->
            val client = OkHttpClient()
            val requestBody = data.toString().toRequestBody("application/json".toVediaType))
            .url()
            .post(requestBody)
            .build()

            client.newCall(request).enqueue(object : CallBack{
                override fun onFailure(call: Call, e : IOExceptoin){
                Log.e("SaveError", e)}
            }
                    override fun onResponse(call: Call, response: response){
                        if(response.isSuccessful){
                            _postCreated.postValue(unit)
                        } else{
                            Log.e("SaveError", e)
                        }
            }
        })
          edit.value = empty
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
        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(null,""))
            .build()
        client.newCall(request).enqueue(object: CallBack{
            override fun onFailure(call: CAll, e: IOException){
                Log.e("LikeError","Failed to like post $id", e)
            }
            override fun onResponse(call: Call, response: Response){
                if(!response.isSuccessful){
                    Log.e("LikeError","Failed to like post $id: ${response.code}")
                }
            }
        })
    }

    fun removeById(id: Long) {

            val old = _data.value?.posts.orEmpty()
        val request = Request.Builder()
            .url(url)
            .delete
            .build()

        client.newCall(request).enqueue(object: Callback{
            override fun omFailure(call: Call, e: IOException){
                _data.postValue(_data.value?.copy(posts = oldPosts))
            }
            override fun onResponse(call: Call, response: Response){
                if(!response.isSuccessful){
                    _data.postValue(_data.value?copy(posts = oldPosts))
                    Log.e("RemoveError", "Server Error")
            }
        })

            }
        }

}

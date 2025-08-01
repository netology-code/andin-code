package ru.netology.handler

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso
import okhttp3.*
import ru.netology.handler.databinding.ActivityMainBinding
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg", "404.png")
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }

            val url = "http://10.0.2.2:9999/avatars/${urls[index++]}"
//            Glide.with(binding.image)
//                .load(url)
//                .placeholder(R.drawable.ic_loading_100dp)
//                .error(R.drawable.ic_error_100dp)
//                .timeout(10_000)
//                .into(binding.image)

            Picasso.get()
                .load(url)
                .error(R.drawable.ic_error_100dp)
                .into(binding.image);
        }
    }
}

/* First version
class MainActivity : AppCompatActivity() {
    private val worker = WorkerThread().apply { start() }
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg")
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }
            worker.download("http://10.0.2.2:9999/avatars/${urls[index++]}")
        }
    }
}

class WorkerThread : Thread() {
    private lateinit var handler: Handler
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun run() {
        Looper.prepare()
        // just for demo: don't use !! & as
        handler = Handler(Looper.myLooper()!!) { message ->
            try {
                val url = message.data["url"] as String
                println("loading: $url")

                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request)
                    .execute()
                    .body?.use {
                        println("loaded: $url")
                        println(it.contentType())
                        println(it.contentLength())
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return@Handler true
        }
        Looper.loop()
    }

    fun download(url: String) {
        println("pass to queue: $url")
        val message = handler.obtainMessage().apply {
            data = bundleOf("url" to url)
        }
        handler.sendMessage(message)
    }
}
 */

/* Second version
class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg")
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper()) { message ->
            // only for demo
            val bitmap = message.data["image"] as Bitmap
            binding.image.setImageBitmap(bitmap)
            return@Handler true
        }

        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }

            val url = "http://10.0.2.2:9999/avatars/${urls[index++]}"
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        response.body?.use {
                            val bitmap = BitmapFactory.decodeStream(it.byteStream())
                            val message = handler.obtainMessage().apply {
                                data = bundleOf("image" to bitmap)
                            }
                            handler.sendMessage(message)
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}
 */

/* Third variant
class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg")
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }

            val url = "http://10.0.2.2:9999/avatars/${urls[index++]}"
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        response.body?.use {
                            val bitmap = BitmapFactory.decodeStream(it.byteStream())
                            this@MainActivity.runOnUiThread {
                                binding.image.setImageBitmap(bitmap)
                            }
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}
 */


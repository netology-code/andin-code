package ru.netology.handler

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import ru.netology.handler.glide.GlideApp
import ru.netology.handler.glide.ProgressTarget
import java.util.Locale

class CustomProgressTarget<Z : Any>(
    target: Target<Z>,
    private val progress: ProgressBar,
    private val text: TextView,
    val url: String?
) : ProgressTarget<String?, Z>(url, target) {

    internal val context: Context = progress.context

    override val granualityPercentage: Float = 0.1f

    override fun onConnecting() {
        progress.isIndeterminate = true
        progress.visibility = View.VISIBLE
        text.visibility = View.VISIBLE
        // TODO Вынести в ресурсы
        text.text = "connecting"
    }

    override fun onDownloading(bytesRead: Long, expectedLength: Long) {
        progress.isIndeterminate = false
        progress.progress = (100 * bytesRead / expectedLength).toInt()
        // TODO Вынести в ресурсы
        text.text = String.format(
            Locale.ROOT, "downloading %.2f/%.2f MB %.1f%%",
            bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength
        )
    }

    override fun onDownloaded() {
        progress.isIndeterminate = true
        // TODO Вынести в ресурсы
        text.text = "decoding and transforming"
    }

    override fun onDelivered() {
        progress.visibility = View.INVISIBLE
        text.visibility = View.INVISIBLE
    }
}

fun CustomProgressTarget<Drawable>.load() {
    GlideApp.with(context)
        .load(url)
        .timeout(10_000)
        // Для примера, убрать в продакшене
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        // Для примера, убрать в продакшене
        .skipMemoryCache(true)
        .error(R.drawable.ic_error_100dp)
        .placeholder(R.drawable.ic_loading_100dp)
        .into(this)
}

package ru.netology.handler.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

abstract class ProgressTarget<T, Z : Any>(
    private val model: T,
    target: Target<Z>,
) : WrappingTarget<Z>(target), UIProgressListener {

    private var ignoreProgress = true

    /**
     * Convert a model into an Url string that is used to match up the OkHttp requests. For explicit
     * [GlideUrl][com.bumptech.glide.load.model.GlideUrl] loads this needs to return
     * [toStringUrl][com.bumptech.glide.load.model.GlideUrl.toStringUrl]. For custom models do the same as your
     * [BaseGlideUrlLoader][com.bumptech.glide.load.model.stream.BaseGlideUrlLoader] does.
     * @param model return the representation of the given model.
     * @return a stable Url representation of the model, otherwise the progress reporting won't work
     */
    private fun toUrlString(model: T): String {
        return model.toString()
    }

    override val granualityPercentage: Float
        get() = 1.0f

    override fun onProgress(bytesRead: Long, expectedLength: Long) {
        if (ignoreProgress) {
            return
        }
        if (expectedLength == Long.MAX_VALUE) {
            onConnecting()
        } else if (bytesRead == expectedLength) {
            onDownloaded()
        } else {
            onDownloading(bytesRead, expectedLength)
        }
    }

    /**
     * Called when the Glide load has started.
     * At this time it is not known if the Glide will even go and use the network to fetch the image.
     */
    protected abstract fun onConnecting()

    /**
     * Called when there's any progress on the download; not called when loading from cache.
     * At this time we know how many bytes have been transferred through the wire.
     */
    protected abstract fun onDownloading(bytesRead: Long, expectedLength: Long)

    /**
     * Called when the bytes downloaded reach the length reported by the server; not called when loading from cache.
     * At this time it is fairly certain, that Glide either finished reading the stream.
     * This means that the image was either already decoded or saved the network stream to cache.
     * In the latter case there's more work to do: decode the image from cache and transform.
     * These cannot be listened to for progress so it's unsure how fast they'll be, best to show indeterminate progress.
     */
    protected abstract fun onDownloaded()

    /**
     * Called when the Glide load has finished either by successfully loading the image or failing to load or cancelled.
     * In any case the best is to hide/reset any progress displays.
     */
    protected abstract fun onDelivered()

    private fun start() {
        OkHttpProgressGlideModule.expect(toUrlString(model), this)
        ignoreProgress = false
        onProgress(0, Long.MAX_VALUE)
    }

    private fun cleanup() {
        ignoreProgress = true
        onDelivered()
        OkHttpProgressGlideModule.forget(toUrlString(model))
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        start()
    }

    override fun onResourceReady(resource: Z, glideAnimation: Transition<in Z>?) {
        cleanup()
        super.onResourceReady(resource, glideAnimation)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        cleanup()
        super.onLoadFailed(errorDrawable)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        cleanup()
        super.onLoadCleared(placeholder)
    }
}
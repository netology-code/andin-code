package ru.netology.handler.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

open class WrappingTarget<Z: Any>(private val wrappedTarget: Target<Z>) : Target<Z> {

    override fun getSize(cb: SizeReadyCallback) {
        wrappedTarget.getSize(cb)
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        wrappedTarget.onLoadStarted(placeholder)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        wrappedTarget.onLoadFailed(errorDrawable)
    }

    override fun removeCallback(cb: SizeReadyCallback) {
        wrappedTarget.removeCallback(cb)
    }

    override fun onResourceReady(resource: Z, glideAnimation: Transition<in Z>?) {
        wrappedTarget.onResourceReady(resource, glideAnimation)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        wrappedTarget.onLoadCleared(placeholder)
    }

    override fun getRequest(): Request? {
        return wrappedTarget.request
    }

    override fun setRequest(request: Request?) {
        wrappedTarget.request = request
    }

    override fun onStart() {
        wrappedTarget.onStart()
    }

    override fun onStop() {
        wrappedTarget.onStop()
    }

    override fun onDestroy() {
        wrappedTarget.onDestroy()
    }
}
package ru.netology.handler.glide

interface UIProgressListener {
    fun onProgress(bytesRead: Long, expectedLength: Long)

    /**
     * Control how often the listener needs an update. 0% and 100% will always be dispatched.
     *
     * @return in percentage (0.2 = call [.onProgress] around every 0.2 percent of progress)
     */
    val granualityPercentage: Float
}

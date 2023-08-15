package ru.netology.handler

interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}

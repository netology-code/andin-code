package ru.netology.nmedia.application

import android.app.Application
import ru.netology.nmedia.auth.AppAuth

class NMediaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.initApp(this)
    }
}
package ru.netology.nmedia.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import org.springframework.context.annotation.Lazy

@Configuration
class FirebaseConfiguration {
    @Lazy
    @Bean
    fun firebaseApp(): FirebaseApp =
        FirebaseApp.initializeApp(
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
                .build()
        )

    @Lazy
    @Bean
    fun firebaseMessaging(firebaseApp: FirebaseApp): FirebaseMessaging =
        FirebaseMessaging.getInstance(firebaseApp)
}
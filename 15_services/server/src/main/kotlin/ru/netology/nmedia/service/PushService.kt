package ru.netology.nmedia.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.netology.nmedia.dto.PushMessage
import ru.netology.nmedia.repository.PushTokenRepository
import org.springframework.context.annotation.Lazy

@Service
@Transactional
class PushService(
    @Lazy
    private val messaging: FirebaseMessaging,
    private val pushTokenRepository: PushTokenRepository,
    private val objectMapper: ObjectMapper,
) {
    fun send(token: String, message: PushMessage) {
        messaging.send(
            Message.builder()
                .putData("content", objectMapper.writeValueAsString(message))
                .setToken(token)
                .build()
        )
    }
}
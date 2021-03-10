package ru.netology.nmedia.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.netology.nmedia.entity.PushTokenEntity
import java.util.*

interface PushTokenRepository : JpaRepository<PushTokenEntity, Long> {
    fun findByToken(token: String?): Optional<PushTokenEntity>
}
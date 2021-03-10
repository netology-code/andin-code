package ru.netology.nmedia.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.netology.nmedia.entity.UserEntity

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String?): UserEntity?
}
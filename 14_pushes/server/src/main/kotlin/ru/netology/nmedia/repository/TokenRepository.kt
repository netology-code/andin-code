package ru.netology.nmedia.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.netology.nmedia.entity.TokenEntity

interface TokenRepository : JpaRepository<TokenEntity, String>
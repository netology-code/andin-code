package ru.netology.nmedia.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
data class TokenEntity(
    @Id var token: String,
    @ManyToOne
    var user: UserEntity,
)

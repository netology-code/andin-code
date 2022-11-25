package ru.netology.nmedia.entity

import jakarta.persistence.*

@Entity
data class PushTokenEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    @Column(unique = true, nullable = false, updatable = false) var token: String,
    // for simplicity save just userId
    var userId: Long = 0,
)

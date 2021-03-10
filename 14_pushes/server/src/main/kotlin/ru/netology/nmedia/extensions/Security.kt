package ru.netology.nmedia.extensions

import org.springframework.security.core.context.SecurityContextHolder
import ru.netology.nmedia.dto.User

fun principal() = SecurityContextHolder.getContext().authentication.principal as User
fun principalOrNull() = SecurityContextHolder.getContext()?.authentication?.principal as? User?

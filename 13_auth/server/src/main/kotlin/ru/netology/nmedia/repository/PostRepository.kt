package ru.netology.nmedia.repository

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import ru.netology.nmedia.entity.PostEntity

interface PostRepository : JpaRepository<PostEntity, Long> {
    fun findAllByIdGreaterThan(id: Long, sort: Sort): List<PostEntity>
}
package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Comment
import ru.netology.nmedia.entity.CommentEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.repository.CommentRepository
import java.time.OffsetDateTime

@Service
@Transactional
class CommentService(private val repository: CommentRepository) {
    fun getAllByPostId(postId: Long): List<Comment> = repository
        .findAllByPostId(postId, Sort.by(Sort.Direction.ASC, "id"))
        .map { it.toDto() }

    fun getById(id: Long): Comment = repository
        .findById(id)
        .map { it.toDto() }
        .orElseThrow(::NotFoundException)

    fun save(dto: Comment): Comment = repository
        .findById(dto.id)
        .orElse(
            CommentEntity.fromDto(
                dto.copy(
                    author = "Student",
                    authorAvatar = "netology.jpg",
                    likes = 0,
                    likedByMe = false,
                    published = OffsetDateTime.now().toEpochSecond()
                )
            )
        )
        .let {
            if (it.id == 0L) repository.save(it) else it.content = dto.content
            it
        }.toDto()

    fun removeById(id: Long): Unit = repository.deleteById(id)

    fun likeById(id: Long): Comment = repository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .apply {
            likes += 1
            likedByMe = true
        }
        .toDto()

    fun unlikeById(id: Long): Comment = repository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .apply {
            likes -= 1
            likedByMe = false
        }
        .toDto()

    fun removeAllByPostId(postId: Long): Unit = repository
        .removeAllByPostId(postId)

    fun saveInitial(dto: Comment): Comment = CommentEntity.fromDto(
        dto.copy(
            likes = 0,
            likedByMe = false,
            published = OffsetDateTime.now().toEpochSecond()
        )
    )
        .let {
            repository.save(it)
        }.toDto()
}
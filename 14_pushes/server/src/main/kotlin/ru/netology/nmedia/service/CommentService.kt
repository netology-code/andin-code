package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Comment
import ru.netology.nmedia.entity.CommentEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.exception.PermissionDeniedException
import ru.netology.nmedia.extensions.principal
import ru.netology.nmedia.repository.CommentRepository
import java.time.OffsetDateTime

@Service
@Transactional
class CommentService(private val repository: CommentRepository) {
    fun getAllByPostId(postId: Long): List<Comment> {
        val principal = principal()
        return repository
            .findAllByPostId(postId, Sort.by(Sort.Direction.ASC, "id"))
            .map { it.toDto(principal.id) }
    }

    fun getById(id: Long): Comment {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .toDto(principal.id)
    }

    fun save(dto: Comment): Comment {
        val principal = principal()
        return repository
            .findById(dto.id)
            .orElse(
                CommentEntity.fromDto(
                    dto.copy(
                        authorId = principal.id,
                        author = principal.name,
                        authorAvatar = principal.avatar,
                        likes = 0,
                        likedByMe = false,
                        published = OffsetDateTime.now().toEpochSecond()
                    )
                )
            )
            .let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }

                it.content = dto.content
                if (it.id == 0L) repository.save(it)
                it
            }.toDto(principal.id)
    }

    fun removeById(id: Long): Unit {
        val principal = principal()
        repository.findById(id)
            .orElseThrow(::NotFoundException)
            .let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }
                repository.delete(it)
                it
            }
    }

    fun likeById(id: Long): Comment {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.add(principal.id)
            }
            .toDto(principal.id)
    }

    fun unlikeById(id: Long): Comment {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.add(principal.id)
            }
            .toDto(principal.id)
    }

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
            it.content = dto.content
            repository.save(it)
            it
        }.toDto(0L)
}
package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Comment
import ru.netology.nmedia.entity.CommentEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.repository.CommentRepository
import ru.netology.nmedia.repository.PostRepository
import java.time.Instant
import java.time.OffsetDateTime

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    fun getAllByPostId(postId: Long): List<Comment> = commentRepository
        .findAllByPostId(postId, Sort.by(Sort.Direction.ASC, "id"))
        .map { it.toDto() }

    fun getById(id: Long): Comment = commentRepository
        .findById(id)
        .map { it.toDto() }
        .orElseThrow(::NotFoundException)

    fun save(dto: Comment): Comment = commentRepository
        .findById(dto.id)
        .orElse(
            CommentEntity.fromDto(
                dto.copy(
                    likes = 0,
                    likedByMe = false,
                    published = Instant.now()
                ),
                post = postRepository.getReferenceById(dto.postId)
            )
        )
        .let {
            val entity = if (it.id == 0L) it else it.copy(content = dto.content)
            commentRepository.save(entity)
            entity
        }.toDto()

    fun removeById(id: Long): Unit = commentRepository.deleteById(id)

    fun likeById(id: Long): Comment = commentRepository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .run {
            copy(
                likes = likes + 1,
                likedByMe = true
            )
        }
        .also(commentRepository::save)
        .toDto()

    fun unlikeById(id: Long): Comment = commentRepository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .run {
            copy(
                likes = likes - 1,
                likedByMe = false
            )
        }
        .also(commentRepository::save)
        .toDto()

    fun removeAllByPostId(postId: Long): Unit = commentRepository
        .removeAllByPostId(postId)
}
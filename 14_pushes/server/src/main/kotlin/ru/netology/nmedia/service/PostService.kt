package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.exception.PermissionDeniedException
import ru.netology.nmedia.extensions.principal
import ru.netology.nmedia.repository.PostRepository
import java.time.OffsetDateTime

@Service
@Transactional
class PostService(
    private val repository: PostRepository,
    private val commentService: CommentService,
) {
    fun getAll(): List<Post> {
        val principal = principal()
        return repository
            .findAll(Sort.by(Sort.Direction.DESC, "id"))
            .map { it.toDto(principal.id) }
    }

    fun getById(id: Long): Post {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .toDto(principal.id)
    }

    fun getNewer(id: Long): List<Post> {
        val principal = principal()
        return repository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.DESC, "id"))
            .map { it.toDto(principal.id) }
    }

    fun save(dto: Post): Post {
        val principal = principal()
        return repository
            .findById(dto.id)
            .orElse(
                PostEntity.fromDto(
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
            .also {
                commentService.removeAllByPostId(it.id)
            }
    }

    fun likeById(id: Long): Post {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.add(principal.id)
            }
            .toDto(principal.id)
    }

    fun unlikeById(id: Long): Post {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.remove(principal.id)
            }
            .toDto(principal.id)
    }

    fun saveInitial(dto: Post) = PostEntity.fromDto(
        dto.copy(
            likes = 0,
            likedByMe = false,
            published = OffsetDateTime.now().toEpochSecond()
        )
    ).let {
        it.content = dto.content
        repository.save(it)
        it
    }.toDto(0L)
}
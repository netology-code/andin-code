package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.repository.PostRepository
import java.time.OffsetDateTime

@Service
@Transactional
class PostService(
    private val repository: PostRepository,
    private val commentService: CommentService,
) {
    fun getAll(): List<Post> = repository
        .findAll(Sort.by(Sort.Direction.DESC, "id"))
        .map { it.toDto() }

    fun getById(id: Long): Post = repository
        .findById(id)
        .map { it.toDto() }
        .orElseThrow(::NotFoundException)

    fun getNewer(id: Long): List<Post> = repository
        .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.DESC, "id"))
        .map { it.toDto() }

    fun save(dto: Post): Post = repository
        .findById(dto.id)
        .orElse(
            PostEntity.fromDto(
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

    fun removeById(id: Long) {
        repository.findByIdOrNull(id)
            ?.also(repository::delete)
            ?.also { commentService.removeAllByPostId(id) }
    }

    fun likeById(id: Long): Post = repository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .apply {
            likes += 1
            likedByMe = true
        }
        .toDto()

    fun unlikeById(id: Long): Post = repository
        .findById(id)
        .orElseThrow(::NotFoundException)
        .apply {
            likes -= 1
            likedByMe = false
        }
        .toDto()

    fun saveInitial(dto: Post) = PostEntity.fromDto(
        dto.copy(
            likes = 0,
            likedByMe = false,
            published = OffsetDateTime.now().toEpochSecond()
        )
    ).let {
        repository.save(it)
    }.toDto()

}
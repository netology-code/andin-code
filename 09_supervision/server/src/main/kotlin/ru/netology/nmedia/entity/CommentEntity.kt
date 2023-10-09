package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Comment
import jakarta.persistence.*
import java.time.Instant

@Entity
data class CommentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val post: PostEntity,
    val author: String,
    val authorAvatar: String,
    @Column(columnDefinition = "TEXT")
    val content: String,
    val published: Instant,
    val likedByMe: Boolean,
    val likes: Int = 0,
) {
    fun toDto() = Comment(
        id = id,
        postId = post.id,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes
    )

    companion object {
        fun fromDto(dto: Comment, post: PostEntity) = CommentEntity(
            id = dto.id,
            post = post,
            author = dto.author,
            authorAvatar = dto.authorAvatar,
            content = dto.content,
            published = dto.published,
            likedByMe = dto.likedByMe,
            likes = dto.likes,
        )
    }
}

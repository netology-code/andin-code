package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Comment
import jakarta.persistence.*

@Entity
data class CommentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    var postId: Long, // no relations for simplicity
    @ManyToOne
    var author: UserEntity,
    @Column(columnDefinition = "TEXT")
    var content: String,
    var published: Long,
    @ElementCollection
    var likeOwnerIds: MutableSet<Long> = mutableSetOf(),
) {
    fun toDto(myId: Long) = Comment(id, postId, author.id, author.name, author.avatar, content, published, likeOwnerIds.contains(myId), likeOwnerIds.size)

    companion object {
        fun fromDto(dto: Comment) = CommentEntity(
            dto.id,
            dto.postId,
            UserEntity(dto.authorId),
            dto.content,
            dto.published,
            mutableSetOf(),
        )
    }
}

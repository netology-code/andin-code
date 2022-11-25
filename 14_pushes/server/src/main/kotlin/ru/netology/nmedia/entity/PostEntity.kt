package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import jakarta.persistence.*

@Entity
data class PostEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    @ManyToOne
    var author: UserEntity,
    @Column(columnDefinition = "TEXT")
    var content: String,
    var published: Long,
    @ElementCollection
    var likeOwnerIds: MutableSet<Long> = mutableSetOf(),
    @Embedded
    var attachment: AttachmentEmbeddable?,
) {
    fun toDto(myId: Long) = Post(
        id,
        author.id,
        author.name,
        author.avatar,
        content,
        published,
        likeOwnerIds.contains(myId),
        likeOwnerIds.size,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            dto.id,
            UserEntity(dto.authorId),
            dto.content,
            dto.published,
            mutableSetOf(),
            AttachmentEmbeddable.fromDto(dto.attachment),
        )
    }
}

@Embeddable
data class AttachmentEmbeddable(
    var url: String,
    @Column(columnDefinition = "TEXT")
    var description: String?,
    @Enumerated(EnumType.STRING)
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, description, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.description, it.type)
        }
    }
}

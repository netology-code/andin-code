package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    var uri: String? = null,
) {
    fun toDto() = Post(postId, authorId, author, authorAvatar, content, published, likedByMe, likes, attachment?.toDto(), )

    companion object {
        fun fromDto(dto: Post) =
            PostWorkEntity(0L, dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe, dto.likes, AttachmentEmbeddable.fromDto(dto.attachment))
    }
}
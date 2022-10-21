package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val isSendToServer :Boolean = false ,
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likedByMe, likes , isSendToServer)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe, dto.likes , dto.isSendToServer)

    }
}

fun List<PostEntity>.toDto(): List<Post> = map { it.toDto() }
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
